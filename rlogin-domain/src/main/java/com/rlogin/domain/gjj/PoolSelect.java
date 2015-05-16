package com.rlogin.domain.gjj;

/**
 * 数据总线
 * @author changxx
 */
public class PoolSelect {

    private String _ACCNUM;         // 3201000273884880
    private String _UNITACCNAME;    // 华为技术有限公司南京研究所
    private String _ACCNAME;        // 路学亮
    private String _DEPUTYIDCARDNUM; // 41052719870606543X

    public PoolSelect() {
        super();
    }

    public PoolSelect(String _ACCNUM, String _UNITACCNAME, String _ACCNAME, String _DEPUTYIDCARDNUM) {
        super();
        this._ACCNUM = _ACCNUM;
        this._UNITACCNAME = _UNITACCNAME;
        this._ACCNAME = _ACCNAME;
        this._DEPUTYIDCARDNUM = _DEPUTYIDCARDNUM;
    }

    public String get_ACCNUM() {
        return _ACCNUM;
    }

    public void set_ACCNUM(String _ACCNUM) {
        this._ACCNUM = _ACCNUM;
    }

    public String get_UNITACCNAME() {
        return _UNITACCNAME;
    }

    public void set_UNITACCNAME(String _UNITACCNAME) {
        this._UNITACCNAME = _UNITACCNAME;
    }

    public String get_ACCNAME() {
        return _ACCNAME;
    }

    public void set_ACCNAME(String _ACCNAME) {
        this._ACCNAME = _ACCNAME;
    }

    public String get_DEPUTYIDCARDNUM() {
        return _DEPUTYIDCARDNUM;
    }

    public void set_DEPUTYIDCARDNUM(String _DEPUTYIDCARDNUM) {
        this._DEPUTYIDCARDNUM = _DEPUTYIDCARDNUM;
    }

    @Override
    public String toString() {
        return "PoolSelect [_ACCNUM=" + _ACCNUM + ", _UNITACCNAME=" + _UNITACCNAME + ", _ACCNAME=" + _ACCNAME
                + ", _DEPUTYIDCARDNUM=" + _DEPUTYIDCARDNUM + "]";
    }

}
