package common.test.crypt.controller;

import common.core.util.R;
import common.encrypt.annotation.Decrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hubby.custom.test.model.vo.PhaseVO;

@Slf4j
@RestController
@RequestMapping("/encrypt")
public class DecryptController {

    @Decrypt
    @PostMapping("/decrypt")
    public R<PhaseVO> no(PhaseVO vo) {

        return R.success(vo);
    }
}
