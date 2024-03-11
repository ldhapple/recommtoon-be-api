package com.recommtoon.recommtoonapi.evaluation.service;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.evaluation.repository.EvaluationRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final AccountRepository accountRepository;

    public Evaluation saveEvaluation(Evaluation evaluation) {
        Long accountId = evaluation.getAccount().getId();
        Long webtoonId = evaluation.getWebtoon().getId();

        Optional<Evaluation> checkExistingEvaluation = evaluationRepository.findByAccountIdAndWebtoonId(accountId,
                webtoonId);

        if (checkExistingEvaluation.isPresent()) {

            Evaluation existedEvaluation = checkExistingEvaluation.get();
            existedEvaluation.updateRating(evaluation.getRating());

            return existedEvaluation;
        }

        return evaluationRepository.save(evaluation);
    }

    public Long getEvaluatedCount(String username) {
        Account loginAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("계정 정보가 존재하지 않습니다."));

        return evaluationRepository.countByAccountId(loginAccount.getId());
    }
}
