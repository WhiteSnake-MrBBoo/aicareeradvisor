package com.example.ai_career_advisor.ServiceImpl.ai;

import com.example.ai_career_advisor.Constant.AiScenario;
import com.example.ai_career_advisor.DTO.ai.AiEssayRequestDTO;
import com.example.ai_career_advisor.DTO.ai.AiEssayResultDTO;
import com.example.ai_career_advisor.Service.ai.AiEssayService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 자기소개서(AI) Stub 서비스 구현체
 *
 * - 실제 AI 연동 전까지는 간단한 문자열 조합으로 동작합니다.
 * - 이후 OpenAI 등 외부 LLM 연동 시 이 클래스를 교체하거나,
 *   별도 구현체를 추가하여 @Primary 등으로 주입 방식을 조정합니다.
 */
@Service
public class AiEssayServiceStubImpl implements AiEssayService {

    @Override
    public AiEssayResultDTO generateEssay(AiEssayRequestDTO requestDTO) {

        String displayName = requestDTO.getDisplayName();
        String targetTitle = requestDTO.getTargetTitle();
        String profileSummary = requestDTO.getProfileSummary();

        List<String> topSkills = requestDTO.getTopSkills();
        List<String> topValues = requestDTO.getTopValues();
        List<String> keyExperiences = requestDTO.getKeyExperiences();
        String mainConcern = requestDTO.getMainConcernSummary();

        //글자들을 계속해서 이어 붙일 때 사용하는 함수
        StringBuilder essay = new StringBuilder();

        /**
         * View 페이지 : 자기 소계서 부분을 지금은 Service 에서 조림 25_1223 보이는 부분만 확인 하기
         * */

        // 1) 인사 및 지원 동기 : 자기 소걔서 내용을 서비스에서 조립 함 ??? 과연 맞을까 ?????
        essay.append("안녕하세요, ").append(displayName).append("입니다.\n\n");
        essay.append("저는 ").append(targetTitle != null ? targetTitle : "희망 직무").append("를 목표로 하고 있으며, ");
        if (profileSummary != null && !profileSummary.isBlank()) {
            essay.append(profileSummary);
        } else {
            essay.append("보유 경험과 역량을 바탕으로 성장 가능성이 높은 인재가 되고자 합니다.");
        }
        essay.append("\n\n");

        // 2) 핵심 역량/스킬
        if (topSkills != null && !topSkills.isEmpty()) {
            essay.append("[핵심 역량]\n");
            essay.append("주요 스킬로는 ");
            essay.append(String.join(", ", topSkills));
            essay.append(" 등이 있으며, 이를 활용하여 프로젝트를 수행해 왔습니다.\n\n");
        }

        // 3) 주요 경험 요약
        if (keyExperiences != null && !keyExperiences.isEmpty()) {
            essay.append("[주요 경험]\n");
            keyExperiences.forEach(exp -> {
                essay.append("- ").append(exp).append("\n");
            });
            essay.append("\n");
        }

        // 4) 직업 가치관
        if (topValues != null && !topValues.isEmpty()) {
            essay.append("[직업 가치관]\n");
            essay.append("저는 '")
                    .append(String.join("', '", topValues))
                    .append("' 가치를 중요하게 생각하며, 이러한 환경에서 가장 높은 성장을 이룰 수 있다고 믿습니다.\n\n");
        }

        // 5) 진로 고민/향후 계획
        if (mainConcern != null && !mainConcern.isBlank()) {
            essay.append("[진로 고민 및 향후 계획]\n");
            essay.append("현재 저의 주요 고민은 '")
                    .append(mainConcern)
                    .append("' 입니다. 이를 극복하기 위해 학습과 실전 프로젝트를 병행하며 역량을 보완해 나가고 있습니다.\n\n");
        }

        essay.append("앞으로도 지속적인 학습과 협업을 통해 조직에 실질적인 가치를 더할 수 있는 인재가 되겠습니다.\n");
        essay.append("감사합니다.\n");

        AiEssayResultDTO resultDTO = AiEssayResultDTO.builder()
                .profileId(requestDTO.getProfileId())
                .scenario(AiScenario.ESSAY)
                .rawEssayText(essay.toString())
                .build();

        return resultDTO;
    }
}
