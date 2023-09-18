package top.hubby.test.custom.http.service;

import common.core.model.Pagination;
import common.core.util.R;

public interface DecryptService {

    R<Pagination> fullEncrypt();

    R<Pagination> fullEncryptV2();

    top.hubby.test.custom.http.model.R<Pagination> partyEncrypt();
}
