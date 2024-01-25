package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.mappers.CourseMapper;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.*;
import com.example.Slearning.Backend.Java.services.PaymentService;
import com.example.Slearning.Backend.Java.services.ProgressService;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    private final LectureRepository lectureRepository;

    private final TrackingCodingExRepository trackingCodingExRepository;

    private final ChapterRepository chapterRepository;

    private final PaymentService paymentService;

    private final CourseMapper courseMapper;

    @Override
    public List<Progress> getProgressCourseOfUser(UUID userId, UUID courseId) {
        List<Progress> progresses = progressRepository.getProgressCourseOfUser(userId, courseId);
        return progresses;
    }

    @Override
    public PageResponse<CourseDto> getMyLearning(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Course> page = this.progressRepository.getCoursesUserHasPayment(pageable, userId);

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
    public Progress getCurrentProgress(UUID userId, UUID courseId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));

        Optional<Progress> progress = this.progressRepository.getCurrentProgress(userId, courseId);
        if(progress.isPresent()) {
            return progress.get();
        } else {
            Payment payment = this.paymentService.getPaymentOfCourse(userId, courseId);
            if(payment != null) {
                List<Progress> progresses = user.getProgresses().stream()
                        .filter(progress1 -> progress1.getLecture().getChapter().getCourse().getId() == courseId)
                        .collect(Collectors.toList());
                if(progresses.size() > 0) {
                    Collections.sort(progresses);
                    return progresses.get(progresses.size() - 1);
                } else {
                    List<Chapter> chapters = course.getChapters();
                    Collections.sort(chapters);
                    List<Lecture> lectures = chapters.get(0).getLectures();
                    Collections.sort(lectures);
                    Progress newProgress = new Progress();
                    newProgress.setCompleted(false);
                    newProgress.setUser(user);
                    newProgress.setLecture(lectures.get(0));
                    return this.progressRepository.save(newProgress);
                }
            } else {
                throw new IllegalStateException("Người dùng chưa mua khóa học này!");
            }
        }
    }

    @Override
    public Progress getNextProgress(UUID userId, UUID courseId, UUID lectureId, Integer grade) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));

        if(course.getStatus().equals(CourseStatus.UN_PUBLISHING)) {
            throw new IllegalStateException("Khóa học không được xuất bản!");
        }

        if(lecture.getPublishStatus().equals(PublishStatus.UN_PUBLISHING)) {
            throw new IllegalStateException("Bài giảng không được xuất bản");
        }

        Chapter curChapter = lecture.getChapter();
        List<Lecture> curLectures = curChapter.getLectures();
        Collections.sort(curLectures);
        Optional<Lecture> nextLecture = curLectures.stream()
                .filter(l -> l.getPosition() > lecture.getPosition())
                .findFirst();
        List<Chapter> chapters = course.getChapters();
        Collections.sort(chapters);
        Optional<Chapter> nextChapter = chapters.stream()
                .filter(chapter -> chapter.getPosition() > lecture.getChapter().getPosition())
                .findFirst();
        Progress progressOfUserAtLecture = getProgressOfLecture(userId, lectureId);
        boolean checkHaveQuestion = curChapter.getQuestions().size() > 0;

        Optional<Progress> currentProgressOptional = this.progressRepository.getCurrentProgress(userId, courseId);
        Progress currentProgress;
        if(currentProgressOptional.isPresent()) {
            currentProgress = currentProgressOptional.get();
        } else {
            currentProgress = getProgressOfLecture(userId, curLectures.get(curLectures.size() - 1).getId());
        }

        if(!nextLecture.isPresent()) {
            // TODO: Là bài giảng cuối chương nên sẽ qua Chapter kế tiếp
            // TODO: Kiểm tra xem chương hiện tại có bài Test không
            if(checkHaveQuestion) {
                // TODO: Có bài Test thực hiện các LOGIC Test
                // TODO Nếu bài giảng cuối chương đã hoàn thành
                if(grade != null) {
                    // TODO: LOGIC khi submit test
                    if(nextChapter.isPresent()) {
                        List<Progress> nextChapterProgresses = nextChapter.get().getLectures().get(0).getProgresses().stream()
                                .filter(progress -> progress.getUser().getId().equals(userId))
                                .collect(Collectors.toList());
                        if (nextChapterProgresses.size() > 0) {
                            // TODO: Nếu đã làm bài Test rồi thì chuyển sang Chapter tiếp theo luôn
                            return nextChapterProgresses.get(0);
                        } else {
                            // TODO: Nếu có bài tập coding thì track
                            if(lecture.getCodingExercises() != null && lecture.getCodingExercises().size() > 0) {
                                TrackingCodingEx trackingCodingEx = new TrackingCodingEx();
                                trackingCodingEx.setCodingExercise(lecture.getCodingExercises().get(0));
                                trackingCodingEx.setCompleted(false);
                                trackingCodingEx.setPrevLectureId(lecture.getId().toString());
                                trackingCodingEx.setNextLectureId(nextChapter.get().getLectures().get(0).getId().toString());
                                trackingCodingEx.setUser(user);
                                trackingCodingExRepository.save(trackingCodingEx);
                            }
                            // TODO: Nếu không thì chuyển đến bài Test
                            // TODO: Nếu đã hoàn thành thì kiểm tra khi nào pass Test mới đến Chapter kế tiếp
                            // TODO: Kiểm tra có pass Test không
                            if (grade >= 0.7 * curChapter.getQuestions().size()) {
                                // TODO: Pass Test nên sẽ cho qua Chapter kế tiếp
                                    // TODO: Chuyển sang Chapter kế tiếp
                                    List<Lecture> lectures = nextChapter.get().getLectures();
                                    Collections.sort(lectures);
                                    Progress newProgress = new Progress();
                                    newProgress.setLecture(lectures.get(0));
                                    newProgress.setUser(user);
                                    newProgress.setCompleted(false);
                                    currentProgress.setCompleted(true);
                                    this.progressRepository.save(currentProgress);
                                    Progress createdNewProgress = this.progressRepository.save(newProgress);
                                    return createdNewProgress;
                            } else {
                                // TODO: Không pass bài Test thì làm lại
                                return getProgressOfLecture(userId, curLectures.get(curLectures.size() - 1).getId());
                            }
                        }
                    } else {
                        // TODO: Là Chapter cuối nên hoàn thành tiến trình khóa học
                        if (!getProgressOfLecture(userId, lectureId).isCompleted()) {
                            // TODO: Chưa hoàn thành Progress hiện tại nên hoàn thành nó
                            progressOfUserAtLecture.setCompleted(true);
                            this.progressRepository.save(currentProgress);
                            // TODO: Nếu có bài tập coding thì track
                            if(lecture.getCodingExercises() != null && lecture.getCodingExercises().size() > 0) {
                                TrackingCodingEx trackingCodingEx = new TrackingCodingEx();
                                trackingCodingEx.setCodingExercise(lecture.getCodingExercises().get(0));
                                trackingCodingEx.setCompleted(false);
                                trackingCodingEx.setPrevLectureId(lecture.getId().toString());
                                trackingCodingEx.setUser(user);
                                trackingCodingExRepository.save(trackingCodingEx);
                            }
                        }
                        return null;
                    }
                } else {
                    // TODO: Nếu bài giảng cuối chương chưa hoàn thành thì đặt là hoàn thành
                    if(!getProgressOfLecture(userId, lectureId).isCompleted()) {
                        currentProgress.setCompleted(true);
                        this.progressRepository.save(currentProgress);
                    }

                    // TODO: Nếu bài giảng cuối chương đã hoàn thành thì trả về Progress của bài giảng
                    return currentProgress;
                }
            } else {
                // TODO: Không có bài Test nên cho qua luôn
                // TODO: Kiểm tra đã hoàn thành trước đó chưa
                if(nextChapter.isPresent()) {
                    // TODO: Kiểm tra xem có phải chương cuối không
                    List<Lecture> lectures = nextChapter.get().getLectures();
                    Collections.sort(lectures);
                    if (!getProgressOfLecture(userId, lectureId).isCompleted()) {
                        // TODO: Chưa hoàn thành nên tạo Progress mới
                        Progress newProgress = new Progress();
                        newProgress.setLecture(lectures.get(0));
                        newProgress.setUser(user);
                        newProgress.setCompleted(false);
                        Progress createdNewProgress = this.progressRepository.save(newProgress);
                        currentProgress.setCompleted(true);
                        this.progressRepository.save(currentProgress);
                        return createdNewProgress;
                    } else {
                        // TODO: Hoàn thành rồi nên cho qua luôn
                        return getProgressOfLecture(userId, lectures.get(0).getId());
                    }
                } else {
                    if(!getProgressOfLecture(userId, lectureId).isCompleted()) {
                        // TODO: Chưa hoàn thành Progress hiện tại nên hoàn thành nó
                        progressOfUserAtLecture.setCompleted(true);
                        this.progressRepository.save(currentProgress);
                    }
                    return null;
                }
            }
        } else {
            // TODO: Không phải bài giảng cuối chương nên sẽ qua bài giảng kế tiếp
            // TODO: Kiểm tra đã hoàn thành trước đó chưa
            System.err.println(currentProgress.getLecture().getTitle() + " " + currentProgress.isCompleted());
            if(!getProgressOfLecture(userId, lectureId).isCompleted()) {
                // TODO: Nếu có bài tập coding thì track
                if(lecture.getCodingExercises() != null && lecture.getCodingExercises().size() > 0) {
                    TrackingCodingEx trackingCodingEx = new TrackingCodingEx();
                    trackingCodingEx.setCodingExercise(lecture.getCodingExercises().get(0));
                    trackingCodingEx.setCompleted(false);
                    trackingCodingEx.setPrevLectureId(lecture.getId().toString());
                    trackingCodingEx.setNextLectureId(nextLecture.get().getId().toString());
                    trackingCodingEx.setUser(user);
                    trackingCodingExRepository.save(trackingCodingEx);
                }
                // TODO: Chưa hoàn thành nên tạo Progress mới
                progressOfUserAtLecture.setCompleted(true);
                progressRepository.save(currentProgress);
                Progress nextProgress = new Progress();
                nextProgress.setCompleted(false);
                nextProgress.setUser(user);
                nextProgress.setLecture(nextLecture.get());
                currentProgress.setCompleted(true);
                this.progressRepository.save(currentProgress);
                return this.progressRepository.save(nextProgress);
            } else {
                // TODO: Hoàn thành rồi nên cho qua luôn
                Optional<Progress> nextProgress = nextLecture.get().getProgresses().stream()
                        .filter(progress -> progress.getUser().getId().equals(userId))
                        .findFirst();
                if(nextProgress.isPresent()) {
                    return nextProgress.get();
                } else {
                    throw new ResourceNotFoundException("Không tìm thấy tiến trình của Lecture", "Id", nextLecture.get().getTitle());
                }
            }
        }
    }

    @Override
    public Progress getProgressOfLecture(UUID userId, UUID lectureId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Optional<Progress> progressOfUserAtLecture = user.getProgresses().stream()
                .filter(progress -> progress.getLecture().getId().equals(lectureId))
                .findFirst();
        if(progressOfUserAtLecture.isPresent()) {
            return progressOfUserAtLecture.get();
        } else {
            throw new ResourceNotFoundException("Progress At", "LectureId", lectureId);
        }
    }

    @Override
    public boolean checkOpenTest(UUID userId, UUID chapterId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Chapter chapter = this.chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        List<Progress> progressesOfChapter = user.getProgresses().stream()
                .filter(progress -> progress.getLecture().getChapter().getId().equals(chapterId)).collect(Collectors.toList());
        boolean checkToOpen = progressesOfChapter.stream()
                .allMatch(progress -> progress.isCompleted() == true) && progressesOfChapter.size() == chapter.getLectures().size();
        return checkToOpen;
    }

}
