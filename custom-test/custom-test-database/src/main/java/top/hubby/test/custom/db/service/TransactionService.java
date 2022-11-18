package top.hubby.test.custom.db.service;

import top.hubby.custom.test.model.dto.PhaseDTO;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
public interface TransactionService {

    public boolean savePhase(PhaseDTO phase);
}
