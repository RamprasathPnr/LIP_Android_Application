package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user1 on 24/5/16.
 */
@Data
public class OtpSmsResponseDto {
    String refNo;
    String queueStatus;
    String refNoDesc;
    String responseMessage;
    String queueMessage;
}
