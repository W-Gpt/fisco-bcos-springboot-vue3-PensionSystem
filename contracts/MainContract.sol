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
    
    function setRosl(address _gongAnRosl,address _laborRosl) public onlyOwner{
        gongAnRosl[_gongAnRosl]=true;
        laborRosl[_laborRosl]=true;
    }
    
    function addSocialSecurityDepartments(address _socialSecurityAddr,string memory _city,uint _maxInsuranceBase,uint _minInsuranceBase,uint _personalContributionRate,uint _companyContributionRate) public onlyOwner{
        require(socialSecurityDepartments[_socialSecurityAddr].socialSecurityAddr==address(0),"社保局地址已存在");
       socialSecurityDepartments[_socialSecurityAddr]=SocialSecurityDepartment(_socialSecurityAddr,_city,_maxInsuranceBase,_minInsuranceBase,_personalContributionRate,_companyContributionRate);
    }
    
    function addCompany(address _companyAddress,string memory _city,string memory _name) public{
        require(socialSecurityDepartments[msg.sender].socialSecurityAddr!=address(0),"只有社保局才能添加公司");
        require(keccak256(abi.encodePacked(socialSecurityDepartments[msg.sender].city))==keccak256(abi.encodePacked(_city)),"社保局与添加公司不在同一城市");
        require(companyInfos[_companyAddress].companyAddress==address(0),"该公司地址已注册");
        companyInfos[_companyAddress]=CompanyInfo(_companyAddress,_city,_name,0);
        socialSecurityOnCompanys[msg.sender].push(_companyAddress);
        companyAll.push(_companyAddress);
    }
    
    function addPersonalInfo(address _personalAddr,string memory _id,uint _age,string memory _name) public {
        require(gongAnRosl[msg.sender],"只有公安才能新增个人信息");
        require(personalInfos[_personalAddr].personalAddr==address(0),"该个人信息已存在");
        personalInfos[_personalAddr]=PersonalInfo(_personalAddr,_id,_age,_name);
        personalAll.push(_personalAddr);
    }
    
    function addLaborInfo(address _personalAddr,uint _salary,address _companyAddress) public {
        require(laborRosl[msg.sender],"只有劳动部门才能新增劳动信息");
        require(personalInfos[_personalAddr].personalAddr!=address(0),"该个人信息不存在");
        require(companyInfos[_companyAddress].companyAddress!=address(0),"目标公司不存在");
        require(labores(laborOnCompanys[_personalAddr][_companyAddress]).personalAddr==address(0),"该公司已有该员工")
        personalInfos[_personalAddr].laborInfoIndex.push(labores.length);
        laborOnCompanys[_personalAddr][_companyAddress]=(labores.length);
        labores.push(LaborInfo(_personalAddr,now,_salary,_companyAddress));
        companyOnPersonalAddr[_companyAddress].push(_personalAddr);
    }
    
    function addPensionAccount(address _personalAddr,address _employer) public{
        require(socialSecurityDepartments[msg.sender].socialSecurityAddr!=address(0),"只有社保局才能添加养老保险账户");
        require(personalInfos[_personalAddr].personalAddr!=address(0),"该个人信息不存在");
        require(labores(laborOnCompanys[_personalAddr][_companyAddress]).personalAddr!=address(0),"该公司没有该员工")
        require(keccak256(abi.encodePacked(socialSecurityDepartments[msg.sender].city))==keccak256(abi.encodePacked(_city)),"社保局与该公司不在同一城市");
        require(pensionAccounts[_personalAddr].employer==address(0),"该个人已存在养老保险账户且有雇主为其缴费");
        pensionAccounts[_personalAddr]=PensionAccount(_personalAddr,socialSecurityDepartments[msg.sender].city,0,0,0,now,false,_employer);
    }
    
    function updatePensionAccount(address _personalAddr,address _employer) public{
        require(socialSecurityDepartments[msg.sender].socialSecurityAddr!=address(0),"只有社保局才能更新缴费");
        require(personalInfos[_personalAddr].personalAddr!=address(0),"该个人信息不存在");
        require(keccak256(abi.encodePacked(pensionAccounts[_personalAddr].employer))==keccak256(abi.encodePacked(_employer)),"该公司不是该保险账户的雇主");
        require(companyInfos[_companyAddress].companyAddress!=address(0),"目标公司不存在");
        require(keccak256(abi.encodePacked(socialSecurityDepartments[msg.sender].city))==keccak256(abi.encodePacked(_city)),"社保局与该公司不在同一城市");
        require(pensionAccounts[_personalAddr].employer!=address(0),"该个人不存养老保险账户");
        uint salary= labores(laborOnCompanys[_personalAddr][_companyAddress]).salary;
        uint max=socialSecurityDepartments[msg.sender].maxInsuranceBase;
        uint min=socialSecurityDepartments[msg.sender].minInsuranceBase;
        uint personalPay=salary*socialSecurityDepartments[msg.sender].personalContributionRate;
        uint companyPay=salary*socialSecurityDepartments[msg.sender].companyContributionRate;
        uint total= personalPay+companyPay > max ? max : ( personalPay+companyPay < min ? personalPay+companyPay);
        
        pensionAccounts[_personalAddr].personalPayments+=personalPay;
        pensionAccounts[_personalAddr].companyPayments+=companyPay;
        pensionAccounts[_personalAddr].totalPayments+=total;
        pensionAccounts[_personalAddr].paymentDate=now;
        companyInfos[_companyAddress].companyPayments+=companyPay;
    }

}