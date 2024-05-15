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
        bool isSponsored; // 是否离职
    }
    //养老保险账号
    struct PensionAccount {
        uint id;//个人身份证号
        string city; // 所在城市
        uint personalPayments; // 个人已缴纳
        uint companyPayments; //公司已缴纳
        uint totalPayments; // 总账户余额
        uint paymentDate; // 缴费时间
        // bool isSponsored; // 是否离职
        address company; // 新增雇主字段
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

    //城市
    struct City{
        address socialSecurityAddr;
        address laodRoslAddr;
    }
    mapping (string=>City) citys; 
    address owner;
    //------------------------------------社保局------------------------------------
    //社保局应该有的功能
    //添加在该社保局缴纳社保的公司
    //查看该公司的缴纳信息
    //查看该公司的参保人数
    //申请迁移
    //同意迁移
    //存储迁移数据
    mapping(address => SocialSecDept) SocialSecDepts; // => SocialSecurityRosl
    mapping(address => bool) SocialSecDeptRoles;
    mapping(address => address[]) AllCompany;//socialSecurityAddr =》 companies 该社保机构里的所有公司
    mapping(address => Company) CompanyByAddr;//Companyaddress => Company
    function addSocialSecDept(string _city,address _socialSecurityAddr,uint _maxBase,uint minBase,uint personalRate,uint companyRate) public{
        // require(owner == msg.sender,"只有合约拥有者才能添加社保局");
        require(SocialSecDeptRoles[_socialSecurityAddr] == false,"社保局已存在。");
        SocialSecDepts[_socialSecurityAddr]= SocialSecDept(_city,_socialSecurityAddr,_maxBase,minBase,personalRate,companyRate); // 添加部门
        SocialSecDeptRoles[_socialSecurityAddr]=true; // 添加角色
        citys[_city].socialSecurityAddr=_socialSecurityAddr;
    }
    function addCompany(address _companyAddress,string _city,string _name,uint _balance) public {
        require(SocialSecDeptRoles[msg.sender],"只有社保局可以添加在社保局缴纳社保的公司");
        require(keccak256(abi.encodePacked(SocialSecDepts[msg.sender].city))==keccak256(abi.encodePacked(_city)),"只能添加本地的公司");
        require(CompanyByAddr[_companyAddress].companyAddress==address(0),"该地址已被使用");
        AllCompany[citys[_city].socialSecurityAddr].push(_companyAddress);
        CompanyByAddr[_companyAddress] = Company(_companyAddress,_city,_name,_balance);
        laodAllCom[citys[_city].laodRoslAddr].push(_companyAddress);
    }
    function getAllCompanyAddr() public view returns (address[] memory) {
        return AllCompany[msg.sender];
    }
    function getCompanyByAddr(address _companyAddress) public view returns (address,string,string,uint) {
        Company memory company = CompanyByAddr[_companyAddress];
        return (company.companyAddress,company.city,company.name,company.balance);
    }
    function approvedTransfer(uint _id) public {
        require(keccak256(abi.encodePacked(ownerApplication[_id].fromSocialSecDept))==keccak256(abi.encodePacked(msg.sender)),"只有转出地社保局才能批准");
        require(ownerApplication[_id].id != uint256(0), "申请不存在");
        require(ownerApplication[_id].status!=2, "社保局已批准");
        ownerApplication[_id].status = 2;
    }
    function acceptTransfer(uint256 _id) public {
        require(keccak256(abi.encodePacked(ownerApplication[_id].toSocialSecDept))==keccak256(abi.encodePacked(msg.sender)),"只有转出地社保局才能批准");
        require(ownerApplication[_id].id != uint256(0), "申请不存在");
        require(ownerApplication[_id].status==2, "转出社保局未批准");
        require(ownerApplication[_id].status!=3, "社保局已接收");
        ownerApplication[_id].status = 3;
        staffs[ownerApplication[_id].toCompany].push(_id);
        staffs[ownerApplication[_id].fromCompany] = removeStaffById(staffs[ownerApplication[_id].fromCompany],_id);

        laborInfos[getLaIndexForC(ownerApplication[_id].toCompany,_id)].isSponsored=false;
        LaborInfo memory laborInfo=laborInfos[getLaIndexForC(ownerApplication[_id].toCompany,_id)];
        // laborInfo.laborInfoIndex=laborInfoIndex;
        laborInfo.companyAddress= ownerApplication[_id].toCompany;
        laborInfo.city=CompanyByAddr[ownerApplication[_id].toCompany].city;
        laborInfo.workDate=now;
        laborInfo.isInsurance=false;
        laborInfos[laborIndex]=laborInfo;
        laborIndexPer[_id].push(laborIndex);
        companyAllper[ownerApplication[_id].toCompany].push(laborIndex);
        laborIndex++;

        PensionAccounts[_id].company=ownerApplication[_id].toCompany;
        // ownerApplication[_id].toCompany;
    }
    
    //------------------------------------公司------------------------------------
    //公司应该有的功能
    //添加该公司下的个人
    //获取该公司下的个人
    //缴纳社保
    //获取缴纳信息
    
    mapping(address => uint[]) public staffs;//公司 => 员工数组
    function getPerByCompany(address _companyAddress) view public returns(uint[] memory){
        return staffs[_companyAddress];
    }
    //获得公司下指定人的工作信息
    function getLaIndexForC(address _companyAddress,uint _id) view public returns(uint){
        for(uint i=0; i<companyAllper[_companyAddress].length; i++){
            laborInfos[companyAllper[_companyAddress][i]].id;
            if (laborInfos[companyAllper[_companyAddress][i]].id==_id){
                return companyAllper[_companyAddress][i];
            }
        }
        return 0;
    }
    function setSalay(uint _id,uint _salay) public {
        uint labIndex=getLaIndexForC(msg.sender,_id);
        require(labIndex!=0,"该公司没有此人工作信息");
        laborInfos[labIndex].salary=_salay;
    }
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
        require(keccak256(abi.encodePacked(account.company))==keccak256(abi.encodePacked(msg.sender)),"你不是该账户雇主");
        SocialSecDept memory social = SocialSecDepts[citys[account.city].socialSecurityAddr];
         salay = salay > social.maxBase ? social.maxBase : (salay < social.minBase ? social.minBase : salay);
        uint Cmoney = salay * social.companyRate;
        uint Omoney = salay * social.personalRate;
        uint Tmoney = Cmoney + Omoney;
        PayMentInfo[index] = PaymentRecord(_id,account.company,social.socialSecurityAddr,account.city,salay,social.personalRate,social.companyRate,Omoney,Cmoney,Tmoney,insuranceDate,paymentDate);
        account.personalPayments+=Omoney;
        account.companyPayments+=Cmoney;
        account.totalPayments+= Tmoney;
        account.paymentDate=paymentDate;
        PensionAccounts[_id]=account;
        //是否参保 为其缴费变为true 获得该公司某个人的工作信息
        laborInfos[getLaIndexForC(msg.sender,_id)].isInsurance=true;
        CompanyByAddr[msg.sender].balance-=Cmoney;
    }
    
    function getPayMentByIndex(uint _payMentIndex) view public returns (uint,address,address,string memory,uint,uint,uint,uint,uint,uint,uint,uint){
        PaymentRecord memory payMentInfo=PayMentInfo[_payMentIndex];
        return (payMentInfo.id,payMentInfo.companyAddress,payMentInfo.socialSecurityAddr,payMentInfo.city,payMentInfo.paymentBase,payMentInfo.personalRate,
        payMentInfo.companyRate,payMentInfo.personalPayments,payMentInfo.companyPayments,payMentInfo.totalPayments,payMentInfo.insuranceDate,payMentInfo.paymentDate); 
    }

    function getPayMentById(uint _id) view public returns (uint[] memory){
        return PayMents[_id];
    }
    //------------------------------------养老保险账号------------------------------------
    mapping(uint => PensionAccount) public PensionAccounts; //根据id获取或者创建养老保险账户
    mapping(uint => Application) ownerApplication;
    function addPenSionAccount(uint _id) public {
        require(PensionAccounts[_id].id != _id,"该用户已有养老保险账户");
        require(PersonById[_id].id!=0,"公安不存在该个人信息，请前往公安登记");
        PensionAccounts[_id] = PensionAccount(_id,"",0,0,0,0,address(0));//初始化养老保险账号信息
        // PersonById[_id] = PersonalInfo(_id,_age,_name);//给公安发一份备案信息
        // staffs[_company].push(_id); //给公司员工数组添加员工id

        // laborInfos[laborIndex]=LaborInfo(_id,_company,CompanyByAddr[_company].city,now,0,false,true);
        // companyAllper[_company].push(laborIndex);
        // laborIndexPer[_id].push(laborIndex);
        // laborIndex++;
    }
    function getPensionInfo (uint _id) public view returns (uint,string,uint,uint,uint,uint,address) {
        PensionAccount memory account = PensionAccounts[_id];
        return (account.id,account.city,account.personalPayments,account.companyPayments,account.totalPayments,account.paymentDate,account.company);
    }
    function applyTransfer(uint _id, address _fromCompany, address _toCompany,address _fromSocialSecDept, address _toSocialSecDept) public {
        require(CompanyByAddr[_fromCompany].companyAddress != address(0), "目标公司不存在");
        require(citys[CompanyByAddr[_fromCompany].city].socialSecurityAddr!=address(0),"该城市社保局不存在");
        ownerApplication[_id] = Application(_id,_fromCompany,_toCompany,_fromSocialSecDept,_toSocialSecDept,1);   
    }
    //------------------------------------公安------------------------------------
    mapping (address => uint[]) public AllPersonID;
    mapping (uint => PersonalInfo) public PersonById;
    address security;
    constructor() public {
        owner = msg.sender; // 将合约部署者设置为合约拥有者
        security=msg.sender;
        laborIndex=1;
    }
    //基本构思City结构体 => 三个角色
    // 设置公安局总局账号
    // function setSecurity(address _security) public {
    //     require(msg.sender == owner);
    //     security = _security;
    // }
    function addPerson(uint _id,uint _age,string _name) public {
        require(msg.sender == security);
        PersonById[_id] = PersonalInfo(_id,_age,_name);
        AllPersonID[msg.sender].push(_id);
    }
    function getAllPersonId() view public returns (uint[] memory){
        return AllPersonID[msg.sender];
    }
    function getPInfo(uint _id) view public returns(uint,uint,string memory){
        PersonalInfo memory personalInfo=PersonById[_id];
        return (personalInfo.id,personalInfo.age,personalInfo.name);
    }
    //------------------------------------劳动局与信息------------------------------------
    // mapping(address => bool)  laodRoles; // 劳动部门角色映射
    mapping(address=> LaodRosl) laodRosls;// 劳动部门角色映射
    mapping(uint=> LaborInfo) laborInfos;//劳动信息索引
    mapping(address=> address[]) laodAllCom;
    mapping(address=> uint[]) companyAllper;//公司的员工工作信息
    mapping(uint=> uint[]) laborIndexPer;//个人的工作索引
    uint laborIndex=1;
    function regLaodongRoles(address _laodRoslAddr,string memory _city) public{
        require(laodRosls[_laodRoslAddr].laodRoslAddr==address(0),"该劳动局地址已注册");
        require(citys[_city].laodRoslAddr==address(0),"该城市已有劳动局"); //判断城市是否已有劳动局
        // laodRoles[_laodRoslAddr]=true;
        laodRosls[_laodRoslAddr]=LaodRosl(_laodRoslAddr,_city);
        citys[_city].laodRoslAddr=_laodRoslAddr;
    }

    //增加劳动信息
    function addLaborInfo(uint _id,address _companyAddress,uint _workDate,uint _salary) public{
        require(laodRosls[msg.sender].laodRoslAddr!=address(0),"只有劳动局才能添加工作信息");
        require(PersonById[_id].id!=0,"不存在该个人信息");
        require(PensionAccounts[_id].id==0,"该个人未注册养老保险账户");
        require(keccak256(abi.encodePacked(laodRosls[msg.sender].city))==keccak256(abi.encodePacked(CompanyByAddr[_companyAddress].city)),"劳动局与公司不在一个城市");
        laborInfos[laborIndex]=LaborInfo(_id,_companyAddress,CompanyByAddr[_companyAddress].city,_workDate,_salary,false,true);
        companyAllper[_companyAddress].push(laborIndex);
        laborIndexPer[_id].push(laborIndex);
        staffs[_companyAddress].push(_id);
        laborIndex++;
        if(PensionAccounts[_id].company==address(0)){
            PensionAccounts[_id].company=_companyAddress;
            PensionAccounts[_id].city=CompanyByAddr[_companyAddress].city;
        }
        
    }

    function getLaodAllCompany() view public returns(address[] memory){
        return laodAllCom[msg.sender];
    }

    function getCompanyAllper(address _companyAddress) view public returns(uint[] memory){
        return companyAllper[_companyAddress];
    }

    function getLaborIndexPer(uint _id) view public returns(uint[] memory){
        return laborIndexPer[_id];
    }

    function getLaborInfo(uint _laborIndex) view public returns(uint,address,string memory,uint,uint,bool){
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