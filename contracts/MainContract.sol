// SPDX-License-Identifier: 3.0
pragma solidity ^0.4.25;

contract MainContract{
    //个人
    struct PersonalInfo{
        uint id;
        uint age;
        string name;
    }
    //社保局
    struct SocialSecDept {
        string city; // 所在城市
        address socialSecurityAddr;
        uint maxBase; // 最大保险基数
        uint minBase; // 最小保险基数
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
        bool isInsurance;
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
        uint[] laborInfoIndex; // 新增员工信息索引
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
    //缴费记录结构体
    struct PaymentRecord{
        uint id; //身份证
        address companyAddress; //公司
        address socialSecurityAddr; //社保局
        string city; // 所在城市
        uint paymentBase; //缴费基数
        uint personalRate; // 个人缴费比例
        uint companyRate; // 公司缴费比例
        uint personalPayments; // 个人缴纳
        uint companyPayments; //公司缴纳
        uint totalPayments; // 总缴纳
        string insuranceDate;//参保年月
        uint paymentDate;//缴费所属时间
    }
    //劳动局角色
    struct LaodRosl{
        address laodRoslAddr;
        string city;
    }
    address owner;
    //------------------------------------社保局------------------------------------
    mapping(string => SocialSecDept) SocialSecDepts; //city name => SocialSecurityRosl
    mapping(address => bool) SocialSecDeptRoles;
    mapping(address => address[]) AllCompany;//socialSecurityAddr =》 companies 该社保机构里的所有公司
    mapping(address => Company) CompanyByAddr;//Companyaddress => Company
    function addSocialSecDept(string _city,address _socialSecurityAddr,uint _maxBase,uint minBase,uint personalRate,uint companyRate) public{
        require(owner == msg.sender);
        require(SocialSecDeptRoles[_socialSecurityAddr] == false,"社保局已存在。");
        SocialSecDepts[_city]= SocialSecDept(_city,_socialSecurityAddr,_maxBase,minBase,personalRate,companyRate); // 添加部门
        SocialSecDeptRoles[_socialSecurityAddr]=true; // 添加角色
    }
    function addCompany(address _companyAddress,string _city,string _name,uint _balance) public {
        require(SocialSecDeptRoles[msg.sender],"只有社保局可以在社保局内添加公司");
        AllCompany[SocialSecDepts[_city].socialSecurityAddr].push(_companyAddress);
        CompanyByAddr[_companyAddress] = Company(_companyAddress,_city,_name,balance);

        
    }
    //------------------------------------公安------------------------------------
    mapping (address => uint[]) public AllPersonID;
    mapping (uint => PersonInfo) public PersonById;
    address security;
    constructor() public {
        owner = msg.sender; // 将合约部署者设置为合约拥有者
    }
    //基本构思City结构体 => 三个角色
    // 设置公安局总局账号
    function setSecurity(address _security) public {
        require(msg.sender == owner);
        security = _security;
    }
    function addPerson(uint _id,uint _age,string _name) public {
        require(msg.sender == security);
        PersonById[_id] = PersonalInfo(_id,_age,_name)
        AllPersonID[msg.sender].push(_id);
    }
    //------------------------------------公安------------------------------------
    mapping(address => bool)  laodRoles; // 劳动部门角色映射
    mapping(address=> LaodRosl) laodRosls;// 社保局映射
    mapping(uint=> LaborInfo) LaborInfos;//劳动信息索引
    mapping(address=> uint[]) LaborAllIndex;
    mapping(uint=> uint[]) laborIndexPer;//个人的工作索引
    function regLaodongRoles(address _laodRoslAddr,string _ctiy) public{
        laodRoles[_laodRoslAddr]=true;
        laodRosls[_laodRoslAddr]=LaborInfo(_laodRoslAddr,_ctiy);
    }
}