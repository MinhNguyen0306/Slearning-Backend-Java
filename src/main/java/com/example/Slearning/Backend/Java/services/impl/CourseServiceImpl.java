package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.mappers.CourseMapper;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.exceptions.ApiException;
import com.example.Slearning.Backend.Java.exceptions.DuplicateException;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.exceptions.UploadFileException;
import com.example.Slearning.Backend.Java.repositories.*;
import com.example.Slearning.Backend.Java.services.CourseService;
import com.example.Slearning.Backend.Java.services.FileStorageService;
import com.example.Slearning.Backend.Java.utils.FileUtils;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import com.example.Slearning.Backend.Java.utils.enums.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = {Exception.class, Throwable.class})
public class CourseServiceImpl implements CourseService {

    @Value("${project.images}")
    private String imagePath;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageStorageRepository imageStorageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseDto getCourseOfPayment(UUID paymentId) {
        Course course = this.courseRepository.getCourseOfPayment(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with Payment", "Id", paymentId));
        return this.courseMapper.courseToDto(course);
    }

    @Override
    public PageResponse<CourseDto> searchCourses(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            String searchKey
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = this.courseRepository.searchCourseByTitle(pageable, searchKey);

        List<Course> content = page.getContent();
        List<CourseDto> courses = courseMapper.coursesToDtos(content);
        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courses);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public CourseDto createDraft(UUID userId, String title) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", userId));

        if(!user.isInstructor() || user.isLock()) {
            return null;
        }

        List<Course> courses = user.getCourses();
        boolean existedCourse = courses.stream()
                .anyMatch(courseEx -> courseEx.getTitle().equals(title));
        if(existedCourse) {
            throw new DuplicateException(Course.class.getName(), "Title", title);
        }

        Course course = new Course();
        course.setTitle(title);
        course.setStatus(CourseStatus.DRAFT);
        course.setUser(user);
        course.setComplete(false);

        Course createdCourse = this.courseRepository.save(course);
        return this.courseMapper.courseToDto(createdCourse);
    }

    @Override
    public String updateTitle(UUID courseId, String title) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setTitle(title);
        Course updatedTitle = this.courseRepository.save(course);
        if(updatedTitle != null) {
            return title;
        } else {
            return "";
        }
    }

    @Override
    public Double updatePrice(UUID courseId, Double price) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setPrice(price);
        this.courseRepository.save(course);
        return price;
    }

    @Override
    public String updateIntro(UUID courseId, String intro) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setIntroduce(intro);
        Course updatedIntro = this.courseRepository.save(course);
        if(updatedIntro != null) {
            return intro;
        } else {
            return "";
        }
    }

    @Override
    public String updateDescription(UUID courseId, String description) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setDescription(description);
        Course updatedDescription = this.courseRepository.save(course);
        if(updatedDescription != null) {
            return description;
        } else {
            return "";
        }
    }

    @Override
    public String updateAchievement(UUID courseId, String achievement) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setAchievement(achievement);
        Course updatedAchievement = this.courseRepository.save(course);
        if(updatedAchievement != null) {
            return achievement;
        } else {
            return "";
        }
    }

    @Override
    public String updateRequirement(UUID courseId, String requirement) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setRequirement(requirement);
        Course updatedRequirement = this.courseRepository.save(course);
        if(updatedRequirement != null) {
            return requirement;
        } else {
            return "";
        }
    }

    @Override
    public ApiResponse updateImageCourse(UUID courseId, MultipartFile imageFile) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        String originalImageName = imageFile.getOriginalFilename();
        String extension = FileUtils.getExtensionFile(originalImageName);
        String name = FileUtils.getFileName(originalImageName);
        try {
            ImageStorage imageStorage = new ImageStorage();
            String fileName = this.fileStorageService.uploadFile(imagePath, imageFile);
            imageStorage.setSize(imageStorage.getSize());
            imageStorage.setExtension(extension);
            imageStorage.setName(name);
            imageStorage.setUrl(fileName);
            imageStorage.setCourse(course);
            ImageStorage savedImage = this.imageStorageRepository.save(imageStorage);
            course.setImage(savedImage);
            course.setStatus(CourseStatus.DRAFT);
            course.setComplete(false);
            course.setAdvertising(false);
            Course createdCourse = this.courseRepository.save(course);
            if(createdCourse != null) {
                return ApiResponse.builder()
                        .message(savedImage.getUrl())
                        .status("201").build();
            } else {
                return ApiResponse.builder()
                        .message("Upload image thất bại. Lỗi Server")
                        .status("500").build();
            }
        } catch (IOException e) {
            throw new UploadFileException(originalImageName);
        }
    }

    @Override
    public ApiResponse updateCourseTopic(UUID courseId, UUID topicId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        Topic topic = this.topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId.toString()));
        course.setTopic(topic);
        Course updated = this.courseRepository.save(course);
        if(updated == null) {
            return ApiResponse.builder()
                    .message("Lỗi server")
                    .status("500").build();
        }

        return ApiResponse.builder()
                .message("Đã cập nhật chuyên đề")
                .status("201").build();
    }

    @Override
    public ApiResponse updateCourseLevel(UUID courseId, Integer levelId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        Level level = this.levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level", "Id", levelId.toString()));
        course.setLevel(level);
        Course updated = this.courseRepository.save(course);
        if(updated == null) {
            return ApiResponse.builder()
                    .message("Lỗi server")
                    .status("500").build();
        }

        return ApiResponse.builder()
                .message("Đã cập nhật cấp độ")
                .status("201").build();
    }

    @Override
    public ApiResponse publishCourse(UUID courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        if(course.getChapters().size() == 0) {
            throw new IllegalStateException("Khoa hoc chua du yeu cau");
        }

        course.setStatus(CourseStatus.PENDING);
        this.courseRepository.save(course);
        return ApiResponse.builder()
                .message("Da gui yeu cau xuat ban")
                .status("200")
                .build();
    }

    @Override
    public PageResponse<CourseDto> getAllCourses(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = courseRepository.findAll(pageable);

        List<Course> content = page.getContent();
        List<CourseDto> courses = courseMapper.coursesToDtos(content);
        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courses);
        pageResponse.setLast(page.isLast());

        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> getCoursesByAdminFetchState(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            AdminFetchCourseState adminFetchCourseState
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page;
        if(adminFetchCourseState.equals(AdminFetchCourseState.HAS_ENROLL)) {
            page = null;
        } else {
            CourseStatus courseStatus = CourseStatus.PUBLISHING;
            switch (adminFetchCourseState) {
                case PENDING -> courseStatus = CourseStatus.PENDING;
                case PUBLISHING -> courseStatus = CourseStatus.PUBLISHING;
                case LOCKED -> courseStatus = CourseStatus.LOCKED;
            }

            page = this.courseRepository.filterCourseByStatus(pageable, courseStatus);
        }

        List<Course> courses = page.getContent();
        List<CourseDto> content = this.courseMapper.coursesToDtos(courses);

        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> filterCoursesByStatus(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId,
            CourseStatus courseStatus
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page;
        if(userId != null) {
            page = courseRepository.filterCourseByStatusOfUser(pageable, userId, courseStatus);
        } else {
            page = courseRepository.filterCourseByStatus(pageable, courseStatus);
        }

        List<Course> content = page.getContent();
        List<CourseDto> courses = courseMapper.coursesToDtos(content);
        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courses);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> filterCoursesByRating(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            Integer rating
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Course> page = courseRepository.filterCourseByRating(pageable, rating);
        List<Course> courses = page.getContent();
        List<CourseDto> coursesView = this.courseMapper.coursesToDtos(courses);

        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(coursesView);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> filterCoursesByTopic(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            List<UUID> topicIdList)
    {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = courseRepository.findAll(pageable);

        List<Course> courses = page.getContent();
        List<Course> coursesByTopics = courses.stream()
                .filter(course -> topicIdList.contains(course.getId())).collect(Collectors.toList());
        List<CourseDto> courseDtoList = this.courseMapper.coursesToDtos(coursesByTopics);

        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courseDtoList);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> filterCoursesByLevel(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            List<UUID> levelIdList
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = courseRepository.findAll(pageable);

        List<Course> courses = page.getContent();
        List<Course> coursesByLevels = courses.stream()
                .filter(course -> levelIdList.contains(course.getId())).collect(Collectors.toList());
        List<CourseDto> courseDtoList = this.courseMapper.coursesToDtos(coursesByLevels);

        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courseDtoList);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<CourseDto> filterCoursesByPrice(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            Integer fromPrice,
            Integer toPrice
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = this.courseRepository.filterCoursesByPrice(pageable, fromPrice, toPrice);

        List<Course> courses = page.getContent();
        List<CourseDto> courseDtoList = this.courseMapper.coursesToDtos(courses);

        PageResponse<CourseDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(courseDtoList);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public CourseDto createCourse(
            UUID userId,
            UUID topicId,
            Integer levelId,
            MultipartFile imageCourse,
            CourseDto courseDto
    ) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", userId.toString()));
        Topic topic = this.topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId.toString()));
        Level level = this.levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level", "Id", levelId.toString()));

        Course course = this.courseMapper.dtoToCourse(courseDto);

        if(!user.isInstructor() || user.isLock()) {
            return null;
        }

        List<Course> courses = user.getCourses();
        boolean existedCourse = courses.stream()
                .anyMatch(courseEx -> courseEx.getTitle().equals(course.getTitle()));
        if(existedCourse) {
            throw new DuplicateException(Course.class.getName(), "Title", course.getTitle());
        }
        user.addCourse(course);
        User updatedUser = this.userRepository.save(user);
        course.setUser(updatedUser);

        topic.addCourse(course);
        Topic updatedTopic = this.topicRepository.save(topic);
        course.setTopic(updatedTopic);

        level.addCourse(course);
        Level updatedLevel = this.levelRepository.save(level);
        course.setLevel(updatedLevel);

        String originalImageName = imageCourse.getOriginalFilename();
        String extension = FileUtils.getExtensionFile(originalImageName);
        String name = FileUtils.getFileName(originalImageName);
        try {
            ImageStorage imageStorage = new ImageStorage();
            String fileName = this.fileStorageService.uploadFile(imagePath, imageCourse);
            imageStorage.setSize(imageStorage.getSize());
            imageStorage.setExtension(extension);
            imageStorage.setName(name);
            imageStorage.setUrl(fileName);
            imageStorage.setCourse(course);
            ImageStorage savedImage = this.imageStorageRepository.save(imageStorage);
            course.setImage(savedImage);
            course.setStatus(CourseStatus.DRAFT);
            course.setComplete(false);
            course.setAdvertising(false);
            Course createdCourse = this.courseRepository.save(course);
            return this.courseMapper.courseToDto(createdCourse);
        } catch (IOException e) {
            throw new UploadFileException(originalImageName);
        }
    }

    @Override
    public List<CourseDto> searchByKeyword(String searchKey) {
        return null;
    }

    @Override
    public CourseDto getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        CourseDto courseDto = this.courseMapper.courseToDto(course);
        return courseDto;
    }

    @Override
    public CourseDto getCoursePublishingById(UUID courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        if(course.getStatus().equals(CourseStatus.PUBLISHING)) {
            return this.courseMapper.courseToDto(course);
        } else {
            return null;
        }
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        course.setDescription(courseDto.getDescription());
        course.setIntroduce(courseDto.getIntroduce());
        course.setAchievement(courseDto.getAchievement());
        course.setRequirement(courseDto.getRequirement());
        return this.courseMapper.courseToDto(course);
    }

    @Override
    public void deleteCourse(UUID courseID) {
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseID.toString()));
        this.courseRepository.delete(course);
    }

    @Override
    public CourseDto unPublishedCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        if(course.isComplete()) {
            if(course.getStatus() == CourseStatus.PUBLISHING) {
                course.setStatus(CourseStatus.UN_PUBLISHING);
            } else if(course.getStatus() == CourseStatus.UN_PUBLISHING) {
                course.setStatus(CourseStatus.PUBLISHING);
            }
        }
        Course updatedCourse = this.courseRepository.save(course);
        return this.courseMapper.courseToDto(updatedCourse);
    }

    @Override
    public ApiResponse resolveCourse(ResolveStatus resolveStatus, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        if(course.getStatus() == CourseStatus.PENDING) {
            if(resolveStatus.equals(ResolveStatus.ACCEPT)) {
                course.setStatus(CourseStatus.PUBLISHING);
                this.courseRepository.save(course);
                return ApiResponse.builder()
                        .message("Đã duyệt xuất bản khóa học")
                        .status("accepted")
                        .build();
            } else {
                return ApiResponse.builder()
                        .message("Từ chối xuất bản khóa học")
                        .status("rejected")
                        .build();
            }
        } else {
            return ApiResponse.builder()
                    .message("Yêu cầu không hợp lệ")
                    .status("400")
                    .build();
        }
    }

    @Override
    public ApiResponse ratingCourse(UUID userId, UUID courseId, Integer rating, String comment) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        Optional<CourseRating> courseRatingOptional = courseRatingRepository.findCourseRating(userId, courseId);
        if(courseRatingOptional.isPresent()) {
            CourseRating courseRating = courseRatingOptional.get();
            if(comment != null) {
                courseRating.setComment(comment);
            }
            courseRating.setRating(rating);
            courseRatingRepository.save(courseRating);
            return ApiResponse.builder()
                    .status("201")
                    .message("Đã cập nhật đánh giá")
                    .build();
        } else {
            CourseRatingKey courseRatingKey = new CourseRatingKey();
            courseRatingKey.setCourseID(courseId);
            courseRatingKey.setUserID(userId);
            CourseRating courseRating = new CourseRating();
            courseRating.setId(courseRatingKey);
            courseRating.setRating(rating);
            if(comment != null) {
                courseRating.setComment(comment);
            }
            courseRatingRepository.save(courseRating);
            return ApiResponse.builder()
                    .status("201")
                    .message("Đã đánh giá khóa học")
                    .build();
        }
    }

    @Override
    public List<CourseRating> getRatingsOfCourse(UUID courseId) {
        List<CourseRating> courseRatings = this.courseRatingRepository.getRatingsOfCourse(courseId);
        return courseRatings;
    }
}
