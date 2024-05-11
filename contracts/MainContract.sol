// SPDX-License-Identifier: 3.0
pragma solidity ^0.4.25;

contract MainContract{
    //个人
    struct PersonalInfo{
        address personalAddr;
        string id;
        uint age;
        string name;
        uint[] laborInfoIndex;
    }
    //社保局
    struct SocialSecurityDepartment {
        address socialSecurityAddr;
        string city; // 所在城市
        uint256 maxInsuranceBase; // 最大保险基数
        uint256 minInsuranceBase; // 最小保险基数
        uint personalContributionRate; // 个人缴费比例
        uint companyContributionRate; // 公司缴费比例
    }
    //公司单位
    struct CompanyInfo{
        address companyAddress;
        string city;
        string name;
        uint companyPayments;
    }
    // 劳动信息结构体
    struct LaborInfo {
        address personalAddr;
        uint joiningDate; // 参与工作时间
        uint  ; // 工资
        address companyAddress;
    }
    //养老保险
    struct PensionAccount {
        address personalAddr;//个人地址
        string city; // 所在城市
        uint personalPayments; // 个人已缴纳
        uint companyPayments; //公司已缴纳
        uint totalPayments; // 总账户余额
        uint paymentDate; // 缴费时间
        bool isSponsored; // 是否离职
        address employer; // 新增雇主字段
    }
    //转移申请
    struct Application {
        address personalAddr;// 申请人
        address fromCompany; // 原城市
        address toCompany; // 目标城市
        bool isApproved; // 是否批准
        bool isHasReceived;
    }
    mapping(address=> PersonalInfo) personalInfos;
    mapping(address=> SocialSecurityDepartment) socialSecurityDepartments;
    mapping(address=> CompanyInfo) companyInfos;
    mapping(address=> PensionAccount) pensionAccounts;
    mapping(address=> address[]) companyOnPersonalAddr;
    mapping(address=> address[]) socialSecurityOnCompanys;
    mapping(address=> mapping(address=> uint)) laborOnCompanys;
    mapping(address=> bool) gongAnRosl;
    mapping(address=> bool) laborRosl;
    address[] personalAll;
    address[] companyAll;
    Application[] applications;
    LaborInfo[] labores;
    
    address owner;    
    constructor() public {
        owner = msg.sender; // 将合约部署者设置为合约拥有者
    }
    modifier onlyOwner() {
        require(msg.sender == owner, "Only contract owner can call this function.");
        _;
    }
    

}