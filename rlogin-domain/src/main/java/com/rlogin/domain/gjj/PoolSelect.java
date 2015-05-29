package com.rlogin.domain.gjj;

/**
 * 数据总线
 *
 * @author changxx
 */
public class PoolSelect {

    private String $page;              // /ydpx/70000002/700002_01.ydpx
    private String _ACCNUM;            // 3201000273884880
    private String _RW;                // w
    private String _PAGEID;            // step1
    private String _IS;                // -1637955
    private String _UNITACCNAME;       // 华为技术有限公司南京研究所
    private String _LOGIP;             // 20150518095012881
    private String _ACCNAME;           // 路学亮
    private String isSamePer;          // false
    private String _PROCID;            // 70000002
    private String _SENDOPERID;        // 41052719870606543X
    private String _DEPUTYIDCARDNUM;   // 41052719870606543X
    private String _SENDTIME;          // 2015-05-18
    private String _BRANCHKIND;        // 0
    private String _SENDDATE;          // 2015-05-18
    private String CURRENT_SYSTEM_DATE;         // 2015-05-18
    private String _TYPE;              // init
    private String _ISCROP;            // 0
    private String _PORCNAME;          // 个人明细信息查询
    private String _WITHKEY;           // 0
    private String sex;//1

    private String accstate;

    public PoolSelect() {
        super();
    }

    public PoolSelect(String $page, String _ACCNUM, String _RW, String _PAGEID, String _IS,
                      String _UNITACCNAME, String _LOGIP, String _ACCNAME, String isSamePer, String _PROCID,
                      String _SENDOPERID, String _DEPUTYIDCARDNUM, String _SENDTIME, String _BRANCHKIND,
                      String _SENDDATE, String cURRENT_SYSTEM_DATE, String _TYPE, String _ISCROP, String _PORCNAME,
                      String _WITHKEY) {
        super();
        this.$page = $page;
        this._ACCNUM = _ACCNUM;
        this._RW = _RW;
        this._PAGEID = _PAGEID;
        this._IS = _IS;
        this._UNITACCNAME = _UNITACCNAME;
        this._LOGIP = _LOGIP;
        this._ACCNAME = _ACCNAME;
        this.isSamePer = isSamePer;
        this._PROCID = _PROCID;
        this._SENDOPERID = _SENDOPERID;
        this._DEPUTYIDCARDNUM = _DEPUTYIDCARDNUM;
        this._SENDTIME = _SENDTIME;
        this._BRANCHKIND = _BRANCHKIND;
        this._SENDDATE = _SENDDATE;
        CURRENT_SYSTEM_DATE = cURRENT_SYSTEM_DATE;
        this._TYPE = _TYPE;
        this._ISCROP = _ISCROP;
        this._PORCNAME = _PORCNAME;
        this._WITHKEY = _WITHKEY;
    }

    public String getAccstate() {
        return accstate;
    }

    public void setAccstate(String accstate) {
        this.accstate = accstate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String get$page() {
        return $page;
    }

    public void set$page(String $page) {
        this.$page = $page;
    }

    public String get_ACCNUM() {
        return _ACCNUM;
    }

    public void set_ACCNUM(String _ACCNUM) {
        this._ACCNUM = _ACCNUM;
    }

    public String get_RW() {
        return _RW;
    }

    public void set_RW(String _RW) {
        this._RW = _RW;
    }

    public String get_PAGEID() {
        return _PAGEID;
    }

    public void set_PAGEID(String _PAGEID) {
        this._PAGEID = _PAGEID;
    }

    public String get_IS() {
        return _IS;
    }

    public void set_IS(String _IS) {
        this._IS = _IS;
    }

    public String get_UNITACCNAME() {
        return _UNITACCNAME;
    }

    public void set_UNITACCNAME(String _UNITACCNAME) {
        this._UNITACCNAME = _UNITACCNAME;
    }

    public String get_LOGIP() {
        return _LOGIP;
    }

    public void set_LOGIP(String _LOGIP) {
        this._LOGIP = _LOGIP;
    }

    public String get_ACCNAME() {
        return _ACCNAME;
    }

    public void set_ACCNAME(String _ACCNAME) {
        this._ACCNAME = _ACCNAME;
    }

    public String getIsSamePer() {
        return isSamePer;
    }

    public void setIsSamePer(String isSamePer) {
        this.isSamePer = isSamePer;
    }

    public String get_PROCID() {
        return _PROCID;
    }

    public void set_PROCID(String _PROCID) {
        this._PROCID = _PROCID;
    }

    public String get_SENDOPERID() {
        return _SENDOPERID;
    }

    public void set_SENDOPERID(String _SENDOPERID) {
        this._SENDOPERID = _SENDOPERID;
    }

    public String get_DEPUTYIDCARDNUM() {
        return _DEPUTYIDCARDNUM;
    }

    public void set_DEPUTYIDCARDNUM(String _DEPUTYIDCARDNUM) {
        this._DEPUTYIDCARDNUM = _DEPUTYIDCARDNUM;
    }

    public String get_SENDTIME() {
        return _SENDTIME;
    }

    public void set_SENDTIME(String _SENDTIME) {
        this._SENDTIME = _SENDTIME;
    }

    public String get_BRANCHKIND() {
        return _BRANCHKIND;
    }

    public void set_BRANCHKIND(String _BRANCHKIND) {
        this._BRANCHKIND = _BRANCHKIND;
    }

    public String get_SENDDATE() {
        return _SENDDATE;
    }

    public void set_SENDDATE(String _SENDDATE) {
        this._SENDDATE = _SENDDATE;
    }

    public String getCURRENT_SYSTEM_DATE() {
        return CURRENT_SYSTEM_DATE;
    }

    public void setCURRENT_SYSTEM_DATE(String cURRENT_SYSTEM_DATE) {
        CURRENT_SYSTEM_DATE = cURRENT_SYSTEM_DATE;
    }

    public String get_TYPE() {
        return _TYPE;
    }

    public void set_TYPE(String _TYPE) {
        this._TYPE = _TYPE;
    }

    public String get_ISCROP() {
        return _ISCROP;
    }

    public void set_ISCROP(String _ISCROP) {
        this._ISCROP = _ISCROP;
    }

    public String get_PORCNAME() {
        return _PORCNAME;
    }

    public void set_PORCNAME(String _PORCNAME) {
        this._PORCNAME = _PORCNAME;
    }

    public String get_WITHKEY() {
        return _WITHKEY;
    }

    public void set_WITHKEY(String _WITHKEY) {
        this._WITHKEY = _WITHKEY;
    }

    @Override
    public String toString() {
        return "PoolSelect [$page=" + $page + ", _ACCNUM=" + _ACCNUM + ", _RW=" + _RW + ", _PAGEID="
                + _PAGEID + ", _IS=" + _IS + ", _UNITACCNAME=" + _UNITACCNAME + ", _LOGIP=" + _LOGIP
                + ", _ACCNAME=" + _ACCNAME + ", isSamePer=" + isSamePer + ", _PROCID=" + _PROCID
                + ", _SENDOPERID=" + _SENDOPERID + ", _DEPUTYIDCARDNUM=" + _DEPUTYIDCARDNUM + ", _SENDTIME="
                + _SENDTIME + ", _BRANCHKIND=" + _BRANCHKIND + ", _SENDDATE=" + _SENDDATE
                + ", CURRENT_SYSTEM_DATE=" + CURRENT_SYSTEM_DATE + ", _TYPE=" + _TYPE + ", _ISCROP="
                + _ISCROP + ", _PORCNAME=" + _PORCNAME + ", _WITHKEY=" + _WITHKEY + "]";
    }

}
