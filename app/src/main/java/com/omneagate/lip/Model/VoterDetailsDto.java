package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user1 on 24/5/16.
 */
@Data
public class VoterDetailsDto extends BaseResponseDto {
    String id;
    String name;
    String dob;
    String dobString;
    String regionalName;
    String mode;
    String address;
    String identityProofType;
    String identityProofNumber;
    String pinCode;
    String gender;
    String age;
    String mobileNumber;
    String emailId;
    String voterId;
    String districtId;
    String constituencyId;
    String otpCode;
    String otpDate;
    String distictName;
    String constituencyName;
    String distictRegionalName;
    String constituencyRegionalName;
    String adminName;
    String adminRegionalName;
}
