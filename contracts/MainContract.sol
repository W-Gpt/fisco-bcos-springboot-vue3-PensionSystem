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
        address companyAddress;
        string city;
        uint workDate; // 参与工作时间
        uint salary; // 工资
        bool isInsurance;//是否参保 第一次为其缴费变为true
    }
    //养老保险账号
    struct PensionAccount {
        uint id;//个人身份证号
        string city; // 所在城市
        uint personalPayments; // 个人已缴纳
        uint companyPayments; //公司已缴纳
        uint totalPayments; // 总账户余额
        uint paymentDate; // 缴费时间
        bool isSponsored; // 是否离职
        address company; // 新增雇主字段
        uint[] laborInfoIndex; // 劳动信息数组
    }
    //转移申请
    struct Application {
        uint id;// 申请人身份证号
        address fromCompany; // 原公司
        address toCompany; // 目公司
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
        uint insuranceDate;//参保年月
        uint paymentDate;//缴费所属时间
    }
    //劳动局角色
    struct LaodRosl{
        address laodRoslAddr;
        string city;
    }
    address owner;
    //------------------------------------社保局------------------------------------
    //社保局应该有的功能
    //添加在该社保局缴纳社保的公司
    //查看该公司的缴纳信息
    //查看该公司的参保人数
    //申请迁移
    //同意迁移
    //存储迁移数据
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
        require(SocialSecDeptRoles[msg.sender],"只有社保局可以添加在社保局缴纳社保的公司");
        AllCompany[SocialSecDepts[_city].socialSecurityAddr].push(_companyAddress);
        CompanyByAddr[_companyAddress] = Company(_companyAddress,_city,_name,_balance);
    }
    function getAllCompanyAddr() public view returns (address[] memory) {
        return AllCompany[msg.sender];
    }
    function getCompanyByAddr(address _companyAddress) public view returns (address,string,string,uint) {
        Company memory company = CompanyByAddr[_companyAddress];
        return (company.companyAddress,company.city,company.name,company.balance);
    }
    function approvedTransfer(uint _id) public {
        // require(SheBaoRole[msg.sender], "社保局才可以批准");
        // require(personalInfo[_id].citys[personalInfo[_id].citys.length - 1] == msg.sender,"只有当前地的劳务局可以批准");
        // require(transferInfo[_id].id != uint256(0), "申请不存在");
        // require(!transferInfo[_id].isApproved, "社保局已批准");
        ownerApplication[_id].status = 2;
    }
    function acceptTransfer(uint256 _id) public {
        // require(SheBaoRole[msg.sender], "社保局才可以接受");
        // require(transferInfo[_id].toCity == msg.sender,"只有转移地的劳务局可以批准");
        // require(transferInfo[_id].id != uint256(0), "申请不存在");
        // require(!transferInfo[_id].isReceived, "社保局已接收");
        ownerApplication[_id].status = 3;
        staffs[ownerApplication[_id].toCompany].push(_id);
        staffs[ownerApplication[_id].fromCompany] = removeStaffById(staffs[ownerApplication[_id].fromCompany],_id);
    }
    
    //------------------------------------公司------------------------------------
    //公司应该有的功能
    //添加该公司下的个人
    //获取该公司下的个人
    //缴纳社保
    //获取缴纳信息
    
    mapping(address => uint[]) staffs;//公司 => 员工数组
    // mapping(uint => uint[]) AllPayMent; // 员工id => 劳动信息数组
    
    // mapping(uint => PensionAccount) accountById;
    // function addStaff(uint _id,uint _age,string _name) public {
        // require(msg.sender == CompanyByAddr[msg.sender].companyAddress);
        // staffs[msg.sender].push(_id);
        // PersonById[_id] = PersonalInfo(_id,_age,_name);
    // }
    // function getStaffs() public view returns (uint[] memory) {
    //     return staffs[msg.sender];
    // }
    mapping(uint => uint[]) PayMents;
    mapping(uint => PaymentRecord) PayMentInfo;
    uint PayMentIndex = 0;
    function PayMent(uint _id,uint salay,uint insuranceDate,uint paymentDate) public {
        uint index = PayMentIndex + 1;
        PayMents[_id].push(index);
        PensionAccount memory account = PensionAccounts[_id];
        SocialSecDept memory social = SocialSecDepts[account.city];
        uint Cmoney = salay * social.companyRate;
        uint Omoney = salay * social.personalRate;
        uint Tmoney = Cmoney + Omoney;
        PayMentInfo[index] = PaymentRecord(_id,account.company,social.socialSecurityAddr,account.city,salay,social.personalRate,social.companyRate,Omoney,Cmoney,Tmoney,insuranceDate,paymentDate);

    }
    

    //------------------------------------养老保险账号------------------------------------
    mapping(uint => PensionAccount) public PensionAccounts; //根据id获取或者创建养老保险账户
    mapping(uint => Application) ownerApplication;
    function addPenSionAccount(uint _id,uint _age,string _name,address _company) public {
        // require(msg.sender == CompanyByAddr[msg.sender].companyAddress);
        PensionAccounts[_id] = PensionAccount(_id,CompanyByAddr[_company].city,0,0,0,0,false,_company,new uint[](0));//初始化养老保险账号信息
        PersonById[_id] = PersonalInfo(_id,_age,_name);//给公安发一份备案信息
        staffs[_company].push(_id); //给公司员工数组添加员工id
    }
    function getPensionInfo (uint _id) public view returns (uint,string,uint,uint,uint,uint,bool,address,uint[]) {
        PensionAccount memory account = PensionAccounts[_id];
        return (account.id,account.city,account.personalPayments,account.companyPayments,account.totalPayments,account.paymentDate,account.isSponsored,account.company,account.laborInfoIndex);
    }
    function applyTransfer(uint _id, address _fromCompany, address _toCompany,address _fromSocialSecDept, address _toSocialSecDept) public {
        // require(employerInfo[_fromCompany].accountAddress != address(0), "目标公司不存在");
        // require(keccak256(bytes(socialSecurityBureauMap[_bureauAddress].city)) == keccak256(bytes(companyMap[_toCompany].city)), "该城市社保局不存在");
        // require(SheBaoRole[_toCity],"该城市社保局不存在");
        ownerApplication[_id] = Application(_id,_fromCompany,_toCompany,_fromSocialSecDept,_toSocialSecDept,1);
        
    }
    //------------------------------------公安------------------------------------
    mapping (address => uint[]) public AllPersonID;
    mapping (uint => PersonalInfo) public PersonById;
    address security;
    constructor() public {
        owner = msg.sender; // 将合约部署者设置为合约拥有者
        laborIndex=1;
    }
    //基本构思City结构体 => 三个角色
    // 设置公安局总局账号
    function setSecurity(address _security) public {
        require(msg.sender == owner);
        security = _security;
    }
    function addPerson(uint _id,uint _age,string _name) public {
        require(msg.sender == security);
        PersonById[_id] = PersonalInfo(_id,_age,_name);
        AllPersonID[msg.sender].push(_id);
    }
    //------------------------------------劳动局与信息------------------------------------
    // mapping(address => bool)  laodRoles; // 劳动部门角色映射
    mapping(address=> LaodRosl) laodRosls;// 劳动部门角色映射
    mapping(uint=> LaborInfo) laborInfos;//劳动信息索引
    mapping(address=> address[]) laodAllCom;
    mapping(address=> uint[]) companyAllper;//公司的员工工作信息
    mapping(uint=> uint[]) laborIndexPer;//个人的工作索引
    uint laborIndex;
    function regLaodongRoles(address _laodRoslAddr,string memory _city) public{
        // require(laodRosls[_laodRoslAddr]==address(0),"该劳动局地址已注册");
        // require(laodRosls[_laodRoslAddr].ctiy!=); //判断城市是否已有劳动局
        // laodRoles[_laodRoslAddr]=true;
        laodRosls[_laodRoslAddr]=LaodRosl(_laodRoslAddr,_city);
    }

    //增加劳动信息
    function addLaborInfo(uint _id,address _companyAddress,uint _workDate,uint _salary) public{
        // require(laodRosls[msg.sender]==address(0),"只有劳动局才能添加工作信息");
        // require(PersonById[_id].id!=0,"不存在该个人信息");
        // require(keccak256(abi.encodePacked(laodRols[_laodRoslAddr].city))==keccak256(abi.encodePacked()),"劳动局与公司不在一个城市");
        laborInfos[laborIndex]=LaborInfo(_id,_companyAddress,CompanyByAddr[_companyAddress].city,_workDate,_salary,false);
        companyAllper[_companyAddress].push(laborIndex);
        laborIndexPer[_id].push(laborIndex);
        laodAllCom[msg.sender].push(_companyAddress);
        staffs[_companyAddress].push(_id);
        laborIndex++;
    }

    function getLaodAllCompany() public returns(address[] memory){
        return laodAllCom[msg.sender];
    }

    function getCompanyAllper(address _companyAddress) public returns(uint[] memory){
        return companyAllper[_companyAddress];
    }

    function getLaborIndexPer(uint _id)public returns(uint[] memory){
        return laborIndexPer[_id];
    }

    function getLaborInfo(uint _laborIndex) public returns(uint,address,string memory,uint,uint,bool){
        LaborInfo memory laborInfo=laborInfos[_laborIndex];
        return (laborInfo.id,laborInfo.companyAddress,laborInfo.city,laborInfo.workDate,laborInfo.salary,laborInfo.isInsurance);
    }

    function removeStaffById(uint[] memory _staffs, uint _id) public pure returns (uint[] memory) { 
        uint length = _staffs.length;
        uint[] memory result = new uint[](length);
        uint counter = 0;

        for(uint i = 0; i < length; i++) {
            if(_staffs[i] != _id) {
                result[counter] = _staffs[i];
                counter++;
            }
        }
        uint[] memory finalResult = new uint[](counter);
        for(uint j = 0; j < counter; j++) {
            finalResult[j] = result[j];
        }
        return finalResult;
    }

}