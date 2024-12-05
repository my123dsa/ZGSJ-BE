package com.example.Finance.service;

import com.example.Finance.dto.AccountInfoResponse;
import com.example.Finance.error.CustomException;
import com.example.Finance.error.ErrorCode;
import com.example.Finance.feign.UserFeign;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInteractService {

    private final UserFeign userFeign;

    public AccountInfoResponse getStoreAccountInfo (Integer storeId)
    {
        try{
            return userFeign.getStoreAccountInfo(storeId);
        } catch (FeignException fe)
        {
            throw new CustomException(ErrorCode.USER_FEIGN_ERROR);
        } catch (Exception e)
        {
            log.error("예상치 못한 에러 발생 : {}", e);
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }

    }
}
