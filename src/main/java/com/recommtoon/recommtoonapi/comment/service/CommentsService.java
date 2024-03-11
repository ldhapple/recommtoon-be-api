package com.recommtoon.recommtoonapi.comment.service;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.comment.dto.CommentRequestDto;
import com.recommtoon.recommtoonapi.comment.dto.CommentResponseDto;
import com.recommtoon.recommtoonapi.comment.entity.Comments;
import com.recommtoon.recommtoonapi.comment.repository.CommentsRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.util.TimeConvertUtil;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final AccountRepository accountRepository;
    private final WebtoonRepository webtoonRepository;
    private final TimeConvertUtil timeConvertUtil;

    public List<CommentResponseDto> getCommentsByTitleId(String titleId) {
        Webtoon webtoon = webtoonRepository.findByTitleId(titleId)
                .orElseThrow(() -> new NotFoundException("웹툰 정보가 존재하지 않습니다."));

        List<Comments> webtoonComments = commentsRepository.findByWebtoonId(webtoon.getId())
                .orElseThrow(() -> new NotFoundException("댓글 정보가 존재하지 않습니다."));

        return webtoonComments.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public void incrementLikeCount(Long commentId) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글 정보가 존재하지 않습니다."));

        comment.updateLikeCount();
    }

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, String titleId, String username) {
        Account loginAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("계정 정보가 존재하지 않습니다."));
        Webtoon webtoon = webtoonRepository.findByTitleId(titleId)
                .orElseThrow(() -> new NotFoundException("웹툰 정보가 존재하지 않습니다."));

        Comments newComment = Comments.builder()
                .account(loginAccount)
                .webtoon(webtoon)
                .content(commentRequestDto.getContent())
                .build();

        Comments savedNewComment = commentsRepository.save(newComment);

        return convertToResponseDto(savedNewComment);
    }

    private CommentResponseDto convertToResponseDto(Comments comments) {
        return CommentResponseDto.builder()
                .id(comments.getId())
                .nickName(comments.getAccount().getNickName())
                .content(comments.getContent())
                .likeCount(comments.getLikeCount())
                .writeTime(timeConvertUtil.convertToLocalDateTime(comments.getCreatedDate()))
                .build();
    }
}
