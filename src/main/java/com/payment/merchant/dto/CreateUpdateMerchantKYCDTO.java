package com.payment.merchant.dto;

import lombok.Data;

@Data
public class CreateUpdateMerchantKYCDTO {
    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    private String dateOfBirth;
    private String placeOfBirth;
    private String gender;
    private String directorBvn1;
    private String directorName1;
    private String directorBvn2;
    private String directorName2;
    private String directorBvn3;
    private String directorName3;
    private String directorBvn4;
    private String directorName4;
    private String directorBvn5;
    private String directorName5;
    private String homeAddress;
    private String bvn;
    private String utilityBill; //base64
    private String governmentIssuedIdCard; //base64

    private String directorValidIdCard; //base64
    private String directorValidIdCardIssuedIdCardExpiryDate;

    private String certificateOfIncorporation; //base64
    private String certificateOfIncorporationExpiryDate;

    private String cac2AndCac7; //base64
    private String cac2AndCac7ExpiryDate;

    private String memart; //base64
    private String memartExpiryDate;

    private String boardResolutionApproval; //base64
    private String boardResolutionExpiryDate;

    private String gazette; //base64
    private String gazetteExpiryDate;

}
