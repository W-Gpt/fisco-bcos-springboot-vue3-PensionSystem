// SPDX-License-Identifier: 3.0
pragma solidity ^0.4.25;

contract MainContract{
    //个人
    struct PersonalInfo{
        uint id;
        uint age;
        string name;
        uint[] laborInfoIndex;
    }
    //社保局
    struct SocialSecDept {
        address socialSecurityAddr;
        string city; // 所在城市
        uint256 maxBase; // 最大保险基数
        uint256 minBase; // 最小保险基数
        uint personalRate; // 个人缴费比例
        uint companyRate; // 公司缴费比例
    }
    //公司单位
    struct Company{
        address companyAddress;
        string city;
        string name;
        uint balance;
    }
    // 劳动信息结构体
    struct LaborInfo{
        uint id;
        uint workDate; // 参与工作时间
        uint salary; // 工资
        address companyAddress;
    }
    //养老保险
    struct PensionAccount {
        uint id;//个人身份证号
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
        uint id;// 申请人身份证号
        address fromCompany; // 原城市
        address toCompany; // 目标城市
        address fromSocialSecDept; //原社保局
        address toSocialSecDept; //转入社保局
        uint status; //审批状态 => 0是保存未提交 1是已提交 2是已经转出 3转入已接收;
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
    //基本构思City结构体 => 三个角色
    
    

}