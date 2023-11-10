package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.mappers.CourseMapper;
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
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    private TopicRepository topicRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageStorageRepository imageStorageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private SearchRepository searchRepository;

    @Autowired
    private CourseMapper courseMapper;

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
    public PageResponse<CourseDto> filterCoursesByStatus(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            CourseStatus courseStatus
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = courseRepository.filterCourseByStatus(pageable, courseStatus);

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
        Page<Course> page = courseRepository.findAll(pageable);

        List<Course> courses = page.getContent();
        List<Course> coursesByPrices = courses
                .stream()
                .filter(
                        course -> course.getPrice() >= fromPrice && course.getPrice() <= toPrice
                ).collect(Collectors.toList());
        List<CourseDto> courseDtoList = this.courseMapper.coursesToDtos(coursesByPrices);

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
            UUID levelId,
            MultipartFile imageCourse,
            CourseDto courseDto
    ) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", userId));
        Topic topic = this.topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId));
        Level level = this.levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level", "Id", levelId));

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
            String fileName = this.fileStorageService.uploadImage(imagePath, imageCourse);
            imageStorage.setName(fileName);
            imageStorage.setSize(imageStorage.getSize());
            imageStorage.setExtension(extension);
            imageStorage.setName(name);
            imageStorage.setUrl(fileName);
            imageStorage.addCourse(course);
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
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        CourseDto courseDto = this.courseMapper.courseToDto(course);
        return courseDto;
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        course.setDescription(courseDto.getDescription());
        course.setIntroduce(courseDto.getIntroduce());
        course.setAchievement(courseDto.getAchievement());
        course.setRequirement(courseDto.getRequirement());
        return this.courseMapper.courseToDto(course);
    }

    @Override
    public void deleteCourse(UUID courseID) {
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseID));
        this.courseRepository.delete(course);
    }

    @Override
    public CourseDto unPublishedCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
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
    public CourseDto resolveCourse(CourseStatus courseStatus, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        if(course.getStatus() != CourseStatus.DRAFT && course.getStatus() == CourseStatus.PENDING) {
            if(courseStatus == CourseStatus.PUBLISHING || courseStatus == CourseStatus.REJECTED) {
                course.setStatus(courseStatus);
                Course updatedCourse = this.courseRepository.save(course);
                return this.courseMapper.courseToDto(updatedCourse);
            }
        } else {
            throw new ApiException("Course is not completed");
        }
        return null;
    }
}
