package common.test.crypt.controller;

import common.core.util.R;
import common.encrypt.annotation.Encrypt;
import common.encrypt.annotation.FullEncrypt;
import common.encrypt.constants.enums.EncryptEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hubby.custom.test.constants.enums.PhaseStatusEnum;
import top.hubby.custom.test.model.vo.PhaseVO;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/encrypt")
public class EncryptController {

    private PhaseVO vo =
            PhaseVO.builder()
                    .id(1L)
                    .phaseCode("CODE")
                    .phaseName("NAME")
                    .endTime(LocalDateTime.now())
                    .status(PhaseStatusEnum.ENDED)
                    .type("2022")
                    .build();

    @Encrypt(encrypt = EncryptEnum.NONE)
    @GetMapping("/get-no-encrypt")
    public R<PhaseVO> no() {

        return R.success(vo);
    }

    @Encrypt(encrypt = EncryptEnum.CUSTOM)
    @GetMapping("/get-custom-encrypt")
    public R<PhaseVO> custom() {

        return R.success(vo);
    }

    @FullEncrypt
    @GetMapping("/get-full-encrypt")
    public R<PhaseVO> full() {

        return R.success(vo);
    }
}
