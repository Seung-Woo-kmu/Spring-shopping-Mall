package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Review;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    @Transactional
    public void add(Review review) {
        reviewRepository.save(review);
    }

    public Review findByReviewId(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findByItem(Item item) {
        return reviewRepository.findAllByItem(item);
    }

    public List<Review> findByGrade(Item item) {
        return reviewRepository.findAllByItemOrderByGradeDesc(item);
    }
    //리뷰가 수정되었을 경우 수정된 리뷰 리턴
    public List<Review> findByNew(Item item) {
        return reviewRepository.findAllByItemOrderByLastModifiedDateDesc(item);
    }

    @Transactional
    public void delete(Review review) {
        reviewRepository.delete(review);
    }
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    @Transactional
    public void deleteByItemId(Long itemId) {
        reviewRepository.deleteByItemId(itemId);
    }
    //해당 회원이 해당 상품에 대한 리뷰를 썼는지 확인
    public Boolean ifFindByItemAndUser(Item item, User user) {
        return reviewRepository.findAllByItem(item).stream().anyMatch(r -> r.getUserName().equals(user.getUserName()));
    }
    //해당 회원이 쓴 모든 리뷰를 리턴
    public List<Review> findByUser(User user) {
        return reviewRepository.findAllByUser(user);
    }
}
