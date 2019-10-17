package comp1110.ass2;

import java.util.*;

public class TestUtility {
    static final int BASE_ITERATIONS = 100;
    static final int PIECES = 8;
    static final int ORIENTATIONS = 8;
    static final char BAD = Character.MAX_VALUE - 'z';

    public static final String[] PLACEMENTS = {
            "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"
    };

    static final String[][] MOVE_SEQUENCES = {
            {"W845HGAYMNZ1VDFEBTUOI0C69RLKQ", "WSGACUXT584MNPD1YZ39FBEQ", "E8WSVUXL9F3YGA45Z0COIHBD76", "0CUI6495ZHJVDAEWSGKL3YMNBT", "8WEAYM45HZNBTU601V79FCDPOIL3"},
            {"G4STBZ1D7PNO6IUW2QE859X3Y0CAFR", "4YSX3ZBTN57820OCIUVD1JPQEW", "4GYSACBZN5TVJIKQR396UOP1028EWXFL", "Q2KEAD71YGSTN546UX3Z0IOCBFR98", "Y1DPOQ2ZN5TSABE84GL9RFCUX306IH"},
            {"YSG48QEABHICOP12WKJL95NMRXTVD76", "128EQKG49RFABCIJ7D", "23F94SUWKLGICOMPDAYZ1J", "I6OCAFRMGYSUW8QE2Z13L945BDPJHK", "CUIGYMA485Z1JL96ONBTSWQEDFRP"},
            {"WQNH5BDEF30UI64GASMPJLRXV789", "I0OM493FRNPQWE2Z1D758", "E8WQRXSA45BDPVU0O671JGHL9F3ZN", "EW28QNMASG45HJVTUC603RXLI", "WQEF39LRM4STZ1VD7582"},
            {"476CADVWEQK2Y1JLX3ZNHIOMP", "SY0IHTWUC6479L3Z12QEADPRFX", "A4MQWECIHZTSVPRLX3Y01JK859FB", "STV79R3Y1DPN5HJKQEW", "SMYACFLXTWKQ28946OI0Z13"},
            {"74MGASTW86ONRL9FBZY02EQPJDCIH", "D7PJGY2Z3RQWU6OCF984SAMNBEKLHTX", "P7468EWSX3L9RFAY0COMGHBDJ", "Y0O64789FAEWSGJVDBCIHLRMN", "Y0C64MAD789RL32EWSUONHJG"},
            {"IGSM49XFL3Z1D7JVPN58EQOU6", "674GIKL3XSMNZY0UOQ8W2EF95BADV1", "AGIUOR3LXVWSM49560Z1JDBE28K", "UOR94MN567VJ1Y2EWSXF3LGABZ0IH", "OIG4STX95HZBADFE2K8WQMR3LJ10U6"},
            {"F94SACB56871YZNTU0OIKJDEQMRL", "9LFACUVD7PMOQR", "LGK8QE2WSUI0OMR9FAYZ1V745HBDC6", "9FLRPD1V74MACUONQEWK82YSTHZ065B", "R9FAYGSUI647DVTHK8QMNZ0CB5"},
            {"5NZ1Y2KEW89FRL3XSMA47PDJVTUICO", "ZNMY102KLFXR394GIUC65TVJ7DP", "NTXR3L94Y0Z1PDVSW82EAC65HGIJ7", "TZN58WVD1JG4SMACE23Y", "ASV78WEQMGY1Z23945TXFLHI06OCD"},
            {"8KLF93RMNTSUIJ1D74YABZH56O02EC", "82EDAYGHJ1P7VSMNRF39LICO0Z564", "P1VJDC60IGHKWXSY3F9L", "KWSVJ1D74MAB5HIL9XF32E86COP", "W857J13Y0COIHBFX94AEK"},
            {"L3RFXST5NBAYG467VD10CUIHK28EW", "LRF3XSGYZNOUW845HBAC67102EDPQKI", "XFLGAS4YZ0ICUT567DE8KHBNR31V", "3FLGS458WE2YZHBACO0UVXRNP76", "3LFA4GYZ560IHBDE28WTNPJ7VUXR"},
            {"XS4MORF3Y0IC6UTVWQED12KGABN578", "3XFABC0UW2E8KGI64SMNQP7VD1Y", "3XFAEK2QMNOILGYSTVW84601P7DBZ", "3XLFABNZHGSYMOPVDCI6UTW2Q84710", "FA465BCDEK28713XTHGIJVSMYZ0OQRL"},
            {"1Y03RX946OCAE8KGIHJ7VSUTNMQ", "1D7VSMQ846IGYAE2KHZ039FRLX", "1DFRQ2KLH54MNBAY039XUICE86O", "Q2YAC684MNHB59LFROUIJDVWEK", "VJKECOQMA46IHBZ03978WSXFD"},
            {"R9FA4SGYMOQ285TUXLHIC0Z17JVDEK", "TSAYG4MOI0UCBDFR97JHZ1PQ8WVXLK", "ZH54MASUVJKE8QPD1YGIL967", "5THZBAMQOUI60Y2K8WSG49XVD13LFR", "BZ54Y06ICADJ71PMQK23XSGHTUV"},
            {"JDAMOPN546USTBEQKW2YZ3RXF98", "POMGADFR94SU603Z21JHBNQWXLIK8EC", "8EBZTNM4569R3FDAGI0OPJVWQ21", "JDP1YSU60OCAM48QNBZ2WEFRL39X", "JP1DA46OICBNHTUWQ28593FRMGKE"},
            {"E89X3FAS467J1PN5HBTUOC0Z", "E84MQKGAY39XTHJ17DFRLIUC0Z5BN", "W8471DASGHTZY3XFL9", "YGL3XUT5HBE8794AC0", "W8EQMRX3Y46579FASTNBC0UIGHJL"},
            {"ZN5469RLF3YABCOP71DE8KWQ", "ZN5TSUXF3LI60CA48KWQMOPJ12EBDV", "ZT5NQO6USA497JI013FLG", "I06UVSGA4MOP1JD75ZBNQECFXL328WK", "N596ICA48WQEFBTSUO0Z13LXVDJGK"},
            {"71JDAFC0IUOMYGK89R3LHNQEWTZ546", "DJ1VSMQKL9R3YAC60IG4785BEF", "1VSMGIKWT5NBA4YZ2039FRX", "1D7JGIC6OMQ84STUX395BZ0", "MYZT5NQE847JVD13XR9FACUIO0"},
            {"7VTWEQK23R9XU64MNOCADFLIGHZY1J", "DVJGASTUCE2QNR9XFB5HZY46I0178W", "JDVSAYG4MNORQWK2ZBTUCI65789FE", "1JVSTW2E85ZBACIGY06OM49R3FXU", "JD71VSTZB54YMOQW8KEACFRLGI693X"},
            {"3RMYG4ABCFX956IUO028QKWSV7JHZ", "X9FBASYGHJ7PVD1ZN546CE8QWKIORM", "9RXSGHJP1VT5NBAY48QOIUWEC603FD7", "J74560UICOMGASWQE289FBZY1DPRXV", "F945HBASGY0OQPDEWUX3R"}
    };

    static final String[][] BAD_MOVE_SEQUENCES = {
            {"ZNP1D7VSMOI6T0YGHKE89LRF3", "YZBTSA48WU67NXLF9RMGI0CDEQNOPJ", "WXL3YZNBTH546DOMP7VSACDEQR98", "WS46OCAMGIJK85ZY1VTB379FEQNP", "8WTNZY10OWGAS45679XFBCEQPJHL"},
            {"YZB54SAC6UI0ONQ21DE279F3LXTVJG", "NOCE2YZT54GABFR9X306IUSVIQ8KHJ7", "45NBZ3XL96IUC0OQ28KEASLV1YGJDF", "USAYZ1P7JKQNBTW820ORX39FLGI6CE", "4571YAOTBCE2Z03F9XLGIK8QNOPVU"},
            {"1D7458KEWQMAYZ39RXLFBCIGJ", "USWQZ2YM45789FRLX31DPJGABCE", "UC648579SRLIJP1DAMGYSTWX3ZBHK2EQNO", "UWO23YA45NBTSX9FRLGI6OCD78KJ1", "IU1OMGSW845TXF9LK2EABCDPRQ"},
            {"E8945ZYGASMOCIH4WQP7VXF3LJDB", "2O84GASMOP7VDFRXLHTZ3Y0695BCIU", "EWQ2Y1V784569FAGMOICBOJHTSUX3RL", "GASTNRV45Z12E86UICOQWVJ79LX3FBH", "8QM4STBZHGL3BXU695NO01VJ7DACE2"},
            {"YMO647PJLXF3TTHIC028EKQNBDVSA", "YMASTVT769F3RLH54", "47693LXSMONBAYZHJKWECDVAU012QP", "HJ139LPAYM47DBC68WEKQONZTSVXR", "AC675493RLFBEQW2KHJR1PMNO0YSTZ"},
            {"Y3Z2EW84ISAF967JDVTNHICUXLRM", "39RX7FASGIU0YZB5HJKWE28476CDV", "2Y03L94GHK8EWSVK6CAD7JPMRFX", "3MRLIU0YZHJDVW284679", "Y0CUSW8467JDEQMAFNNHGL32"},
            {"UVPMG665TSWQ2E8479FAY0O", "6OIUSG4MRXVD1FN578WE239FABHL", "0I6OMTSG47PR3ZTBDV1JHK2W89XFE", "CUVWEQR9LX3YM482ZBDPNOIGST5HJ71", "DPR3XSVJKE8W2Y0IGV6OMAFLH571"},
            {"LI6UCVYA4MQKH57D1PNBF98W2ZTS", "LIU6OIN5THBAYGS4897PQ2EWKJ1DC03FR", "L9RFA4SGYZH5TNMQEKIJ10678O2", "9LFAMQ2YGST65THBCDVJ710IK8EW", "U6OI0YACBN54SMQP713RF98WEDVJHKNZT"},
            {"N5THG4SADPJVUO60IKQE879FJX3Y1Z2W", "TV78WEQ2Y456COMADFRMX93Z1JGIUS", "ZY1P76UOC03RFX94MATIJDEQ8W2", "T5NMYSGIUCA47JK8WE2ZHLRO01PVDFO39", "ADE8WK2YM4GIHJLRNOPVTZ1786USXF39"},
            {"MAS4Y0CO65BEF97PNHJIKLRXTVW2C1", "8E2KL3XF94SAMNO0JIUTHZY17PVDB5", "KE21398WSLY0IC6457PONHBADVJGLXF", "R93Y0US48KGHLXFACOI657ODPMN", "K82YM47JV1PDAECOIGHTSUWX9LPF3Z5N"},
            {"3MXLGI6OCU0YZH5NMRPQ8KEASV74", "845HGAMOMNPVSUWE2YZBD7JLX3FR", "FLKE2WD4GAY3RNTUCOPV75Z06", "X3FRMNQ28WU6CD7VUZ54YAEKGHL", "RLFXSW2YG456U0P13"},
            {"XFAEBCUI60YM47PJGKQNR312WQT58", "XLIMY123FASMON54GJ78KEBCUVDPQW", "XL3FBCIUO0YAD7P1R84GSMNQEKHTW", "3FXLGICUOM4SABDEWQ285ZINPV1Y067", "LFAMNB5HGI6USX3Y012EQ647DC"},
            {"J17VSM4689L3ZBTOIGK2QRFACEWUON", "DVXTS493ZH58KGABCUOMNQRRE21JL", "DVJ174Y3958EWQMASTXFLRNQCOU0IHZ", "DVJ1YMNHGACB5460IKLJROQW23X978E", "JIK8QOM46USABZHLRFXVD1398"},
            {"BTHZY3BFXU01DVSAC648QEKIGJ79RM", "ZHBTSM4Y0COIGAD71PJKL9FJQ8WUVX", "MOQ28KLXFXI64931DVJGHBAE", "MOIYSTBC0ZH54GY1VX9678KEQ2", "B5TSA2G4MO60CUX97V12Q8EDJIKLFRP"},
            {"10I6UCOM48EQNRX39FABTHZ2WV4JG", "1DAG4658QE3TNHI0COMPJLR3Z2", "DA4GI0ZNBTWQ21JPMO65X9R3FC", "1JIC0O64GKW2QRMAYUBEDFXT5NP", "JD1YA4MOC0Z5NBEQ2WTX968KGIHLFR4"},
            {"8WEQMABZYGH4LR3XF94ST5NOP71DCIU0", "EQ247PNZYGHBACUIL93FXSMO01DJ", "WSG4671PMABCDFRLXTPO0ZN5HIJK8QE", "Y0ON5BA4MQRFX3M6UTV78EDCIHJKW", "K8471YMNO0CABTHGJL3NXFDVP"},
            {"ZTN54Y3FOSABCD10IU68EQWKGJL9", "5NMAS482QEWKGIC06OPVTUXL9R3ZBD2J", "J7PMOC69FXSK4ABD1ZT5NQKWU", "TUIGA4MXNB5Z01DFXL328KEC697JPQW", "NZYGAB049FE8KIUOCDP7601JLXSMQWT"},
            {"KYMACO60IGSUTNZ23RFXVJKW84579", "1YZNMQR39FASTWK84AH57VDCE", "QE8WK1MRF3YA45TSU671DBCI0", "D10CO6USGYMQR9XTV748WK2Z7AEFLIJ", "D1YZ20U641STN578EWKHBACFRLIJVX39"},
            {"QNO6ICAYZTBAEWK21J784MR95", "1V7485ZTBE2WQMOIC0YWADFLR9X", "OQWKLFRXSMNTU64YGH5Z397QEACDB", "1V78938FASGHZ5TUW2Y0C6IJDBEKQRN", "V1J74YZT58WEF9LRUSACBD"},
            {"R3Z0UC60MSWQN5HBAY2849XVJGKED7", "39FRMAQUI60ON54GYZBCE287JVTWX", "F3X9457VJD1YMORNQWSABCUIGUK82E", "945671ZYBAMGHBTW8ECIUOPVXFR302Q", "3FRNPV79XSTBZY0IAUOQ8E21DJG45"}
    };

    static final String[][][] SUPPORTERS = {
            {{"a0b5c0c3c4e2e3f0", "a1a2a4a6b1b4c1d2e0", "b2b6d0d1e1f1f2g0", "a3a5a7c2c5d3d4g1"}, {"a7b4b5d3e0e1f1", "a0a5a6d1d2d4f2", "a1a2a4b0b1b2b3b6c5", "a3c0c1c2c3c4f0g0"}, {"a4b2c0c1c2c3c4c5e2f2", "b1d1d2e0e1e3g0g1", "a0b0b3b5b6f1", "a1a3a5a7d0d3d4"}, {"a0a1a5a6b0b1b3f2g1", "a3a4a7b6c1e3f1g0", "a2c0c2c5d1d4f0", "b4b5c4d0d2d3e2"}, {"a3a4a5a7c1d2d4e0e1e3", "b5c4d1e2f0f1f2", "a1b2b4b6c2c3c5d0g1", "a0a2a6b0b1b3c0d3g0"}},
            {{"a0a4b0b3b4b5b6c3c4d1d4", "a3a6b1b2c2e2f0f1g0", "a5c0c1c5d2e1e3", "a1a2a7d0d3e0f2g1"}, {"a6c3d2e1e3f2g0", "a2a3a5a7b5c2c4d1", "a0a4b0b3b4c1d0f0", "a1c0c5d3e0f1"}, {"a2a5a6b2b5c2e1g1", "b6c0c3c4c5d0f2g0", "a0a1a7d1d3d4e2f0", "a3a4b0b1b3b4c1d2e3f1"}, {"a0a1a5c2d1e1f0f1g1", "b6c3c4c5d4e0g0", "a2a6a7b0b1b2b4b5c1d0f2", "a3b3c0d2d3e2e3"}, {"a6b2c5d0d1e1g0g1", "a0a4b6c4d2d4f1f2", "b1b5c0c2c3d3e0e3", "a1a2a3a5a7b3c1e2f0"}},
            {{"a5a7b0b3b4d0d1e0e1e3", "b2b6c2c5d2e2f2g0", "a0a1a2c4d3d4f0f1g1", "a3a4a6b1b5c0c3"}, {"c2c5e0e1e2f1", "a4a6a7b2c3d0", "a0c1d1g0g1", "a1a5b5c0f0"}, {"a6a7b2c0c2c3c4f1", "b0b1b5d2e3f0", "a2a3b3c1d3d4g0g1", "a0a4a5b4c5e1"}, {"a0a4a5a6b1b5b6e0g0g1", "a7b2b3b4c1c4d1e1", "b0c0c2c5d0d2e2f1", "a1a2a3c3d3d4e3f0f2"}, {"a7b0b3c3d1d2f0", "a2a3a4a5b1b2c1d3f2", "a0b4b5d0e0e1e2e3", "a1c0c2c4c5d4f1g0g1"}},
            {{"b1c2c3c5e1e3g1", "a2a6a7b3c1c4d2d3", "a0a1a4a5b2b5d1d4f0f1", "a3b4c0d0e0e2g0"}, {"a0a2a4b1b2b6d1e1", "a5d0d4e2f1", "b4c1d3f0f2", "b0b5c2c4e3"}, {"a0b2b4b5b6c5d0e2f0f1", "a2a3a4c2e3g0g1", "a6a7c0c1d2d3e0e1", "a1a5b0b1b3c3c4d1d4"}, {"b1c0c1c4d0d2e0", "a0a1a3c5e1f0f1", "a2b2b5b6f2g0g1", "a4a7b3c2c3d4"}, {"b0b4b5c1d3e1", "b2b6c2c4d1d4e2", "a2a4b3d0d2g1", "a0a5a7e3f2"}},
            {{"a0a3a6b5d4e2e3g0", "a1a4a5b1b3b4b6e1", "a7b0c4d2f0f2", "a2b2c0c1c2c3c5g1"}, {"c0d0d1d3e0e1e2g0", "a0a2a3b1b4c2d2f1", "b3b5b6c3e3g1", "a1a4a5a6b0c1c4d4"}, {"a1a3a5c1c4e1e2g0g1", "a7b0b3b5d2d3d4e3", "a0a2a6c0c5d0f1f2", "a4b1b2b4b6c2c3d1e0"}, {"a1b6d1d3e0e1", "a3a6b1d4f1g0", "a4a7b3c1c4e3g1", "a0a2a5b2f2"}, {"a2b0c0d2e0f1", "c5d0d1d4e1f0g1", "a0a4b3c2d3f2", "a3a6b5b6c1c3e2"}},
            {{"b1b2d0e0e3f1g0g1", "a5a6a7d2d3d4e2f0", "a3a4b3b6c1c2d1e1f2", "a0a1a2b0b4b5c0c3c4c5"}, {"b0b2b3b4c3e2f0f1", "a3b1d1d3d4e0f2g1", "b5b6c0c1c2c4d0e1e3", "a0a1a2a4a5a6a7c5d2g0"}, {"a0a7b3b5c0c3c4e0e3g0", "a2a4b0b1b4d0d4f1", "a5a6b2b6c1c2c5d2g1", "a3d1d3e1e2f0f2"}, {"a1a5a6b0b4b5d1e2f0f1f2", "a0a7b1b2c3d2g1", "a4b3b6d4e0e3g0", "a2a3c0c1c2c4c5d3e1"}, {"a1a5a6b1c1c2c5d1d2", "a0a7b0b5e1e3f0f1f2", "a2a3a4b3d0g0g1", "b6c3d3d4e0e2"}},
            {{"a0b1b2b5d0d2f1f2g1", "a1a5c1c4e0f0", "a3a6b6d1d4e1e2e3", "a4a7b3c0c3c5d3"}, {"a3b0b5b6c0c2d1e3g1", "a0a1a2b1d0d2d3d4e0e1e2", "a6a7b2b3b4c3f2", "a4a5c1c4c5f0f1g0"}, {"b4b6c2c3d1d4g1", "a0a1a4a6b1b2b3c0f0f1", "a2a3a5b0b5c1d2g0", "c5d0d3e0e1e2e3f2"}, {"a2a5b0b2b3c5d3g0g1", "a3b1c3d1e0e2f2", "a0a1a4a6b6c0c2c4f0", "a7b4b5c1d0d2d4e1e3"}, {"a1b4c2c5d1e0e2e3f2", "b0b5c3c4d2d4e1f1g1", "a0a3a4a7b3b6d3f0", "a2a5a6b1b2c1d0g0"}},
            {{"a0a5c0c3c5d0d4g0", "a4a7b1b4b6e0f0f2", "a6b0c1c4d1d3f1g1", "a1a2a3b2b3b5e2e3"}, {"a4a7f0g0g1", "a0a3b3d1d3", "a5b1d2", "b0b6c1c4c5"}, {"a1a5b3b5c4e2e3f0g0", "a0a2b1b2c0c3c5d2", "a3a6c2d0d1d3d4g1", "a4a7b0b4e0e1f1f2"}, {"a4a7b4c3d3e2f0g0g1", "a0a2a3a5b0b2b5b6c2f1", "a1a6b1b3c4d0e0e1", "c0c1c5d1d2d4e3f2"}, {"a3a6b0b6c1d0d1d4", "a4a7b4b5c0c3c4d2d3e0", "a5e2e3f1f2g0", "a0b1b2c5f0g1"}},
            {{"b0b3b4b5c3c5d3d4f2g1", "a3b1c0c4e0e3f0f1", "a2a5b2b6c2d0d1e2", "a0a1a6a7c1d2e1g0"}, {"a4a5b6c5d2d4e0g0", "a2a6b1b2b3c4e2", "a0a3b4b5e1f0f1", "a1a7b0c0c3d0d1g1"}, {"a4b0b1b6c2c5f1f2", "a7b2b3b4c4e0e3", "a2a3a5b5c0c3e1e2g0", "a0a1c1d2d3d4g1"}, {"c1c4d2d3e3", "a1a3a5f0f2", "a4a7b1b5b6c2", "a0b0b2b3b4d4"}, {"a0a5a6b2b3c2e3f0f1", "a3a7c3d3d4e0f2", "a1a4b0b5b6c0c1c4", "a2b4c5d0d1d2e1e2g0"}},
            {{"a2c1c2c5d1d4e0f1g1", "a0a3a7b0b2c3d2f0", "b1b3c4d0e1e3g0", "a1a4a5a6b4b5c0d3"}, {"a0a2a4b1b3b6c3e0e1e2", "a1b0b4d0d2d3e3f0f1g0", "a5a6a7b2b5c1c5f2", "a3c0c2c4d1d4g1"}, {"a1a7c0c1f1f2", "a5b1b2b4e2g0", "b6c4d0d2d3d4g1", "a0b3e1e3f0"}, {"a0a1a6b2b3c3f0g1", "a3b5c1c2c4c5e2g0", "a4a5b1b4c0d0d2d3e0f1f2", "a2a7b6d1d4e1"}, {"a0b0b3c1c4f0g1", "a2b2c3e0g0", "a1a3a4a5a6a7b4", "c0c5d1d4e1e2e3f1"}},
            {{"a3a4a7b0b1b2b4b6c1d1", "a0a1a2a5c2c5e0f2g0g1", "b3b5c0d2d3d4e1f1", "a6c3c4d0e2e3f0"}, {"a3b1b2b4c0c2c5d1f0f1", "a0b3b6c1d0e0e1e2", "a2a4a7b5c3d2d3d4f2", "a1a5a6b0c4e3g0g1"}, {"a3b2b5c1c2c5d0e1g1", "a0a2a4a5a6c3f0f1g0", "d1d2d3d4e0e2e3f2", "a1a7b0b1b3b4b6c0c4"}, {"a0a4a5b0b1b2b4g0", "a3c3d0d2d3d4e2f0f1f2", "a6c0c1c2c5d1e0e1e3", "a1a7b3b5b6c4g1"}, {"b1b6c1c2c5e3f0f2g0", "a0a4a5a7b3c0d1e0g1", "a1a2a3a6b4c3c4", "b0b2b5d0d2d3d4e1e2f1"}},
            {{"a1a2a4a6b1b2e1e2e3", "b0c1c2c3c5d1d3d4f0", "a3b3b5d0d2e0g0", "a0a5a7b4b6c0f1f2g1"}, {"a2b4b5d4e0f0f1f2", "a0a5a7b1b2b6c1c5d1", "a1a3a4a6d0d3g0g1", "b0c0c2d2e1e2e3"}, {"a3a4b3b4b5c1c4f2g1", "a0a5a6a7b2c0c2d2e0e3", "a1a2c5d0d3e1f1", "b0b1d1d4e2f0g0"}, {"a0b1c2e2f0f1f2g1", "a5b0b2b3b6d3d4e1", "a2b4b5c1c3c4d1d2g0", "a1a4a6c0c5d0e3"}, {"a1a3a6a7b3b4d0f0", "a0a4b1c0c1c3d2d4e0e2", "a5b5b6c4c5d1d3f2", "b0b2e1e3f1g0g1"}},
            {{"a0a3b0b1b3b6d0e3g1", "c0c3c5e2f1f2g0", "a1a2a4a6a7b4c2c4d2", "a5b2b5c1d1d3d4e0"}, {"a1a2b0b5c0e0e2f0", "a0a3b3c3c4c5d4f2", "a5a6b1b4b6c1c2d2e1", "a4b2d0d3f1g1"}, {"a4b0b4c2c3d2e3f1", "a0a3a5a6b2b3b5b6d1", "a1a2a7c4d4e0e1g0", "b1c0c5e2f0f2g1"}, {"a0a5a6a7b1c0c2c5d2g1", "a2a3a4b2b3c1c4e3g0", "b4c3d0e2f0f1f2", "a1b6d1d3d4e0e1"}, {"a1a7b0b2b3b5c3d0d3d4", "c0c2c4d1e0e2g0", "a0a2a3a5a6c1d2f2", "a4b1b4b6c5e1f1"}},
            {{"a3a6a7b5c4c5d1d2", "a0a5b1b2b4b6c0d0d3g0", "a2a4c2d4e0e3f0g1", "a1b0b3c1c3e1e2f1f2"}, {"a2a6b0b1b2b6c2e1f0", "a5b4b5c1c3d1e2f1g0", "a0a4a7b3c5e3f2g1", "a1a3c0c4d2d4e0"}, {"a6b6c2c5f1g1", "a0a1a5b1b2b3d4e0", "b4d0d3e3f0g0", "a7b5d1e1e2f2"}, {"a4a5a6b0c0d0d1e2", "a1a2a3b1b3b5c1f1f2", "b2b4c3c4c5d4e1", "b6c2d3e0e3f0g0g1"}, {"a1a2b0b5c4d2e0", "a5b3b4c2f1f2g0", "c1c3d0d3e2e3f0g1", "a0b2c0c5d1d4e1"}},
            {{"a6b2c3c4c5d4e0e1e3", "a3a7b6c1e2f0g1", "a0a2a5b1b3b4b5f1f2", "a1a4c0d0d1d2d3"}, {"a2a7b5c0c1f0f1g1", "a5b4b6c4d0d4e1f2", "a0a3a4c3d1d2d3e0e3g0", "a6b0b1b2b3c2c5e2"}, {"a1a2a3b0b2d2f0", "a5a6b5c0c1c3c4e3f2g0", "a0a4a7b1b4b6d0e0g1", "b3c2c5d1d3d4e1e2f1"}, {"a4b4c4d1e0e2e3f2", "a3a5a6a7b6d3d4e1", "a0a1a2b1b2c2c3c5f0g1", "b3b5c0c1d0d2f1"}, {"a4c2d2e0e3f1g0g1", "a0a3b0b2b5d1d3d4f0", "a2a6a7b1c0c1c3c4d0", "a1a5b3b6e1e2f2"}},
            {{"a1a4a6c2e2g0g1", "a2a5a7c3c4d1f0f1f2", "b6c0c5d0e0e1e3", "a3b0b1b2b3b4b5d4"}, {"a1b6c0c1c5d1d2e2e3g1", "a3c2c3d3e0f0f1g0", "a0a5a6a7b2b4d0d4", "a2a4b0b3b5c4e1f2"}, {"a0a1a4c1f0", "c2d0e0f1g0", "a5a6b0c0c5e1", "a2a3a7b2b3b5b6"}, {"a1a3a4b0b6c4g0", "a0d0d4e2e3f2", "a5a6c1c3c5g1", "b2c2e1f0f1"}, {"a0a1a3a4b3b5b6c4f0", "a2a6c5d0e2e3f1g0", "a5a7b0b4c0d4g1", "b2c1c2c3d2d3e0e1f2"}},
            {{"a1a3a4a6b6c0e0", "a0c1c5d0f1f2", "a2b4b5c3c4d1d2d4", "b0b1b2d3e1e2f0g0"}, {"a0a1a4b1b2b4c1e0e3", "a6a7b5c5d2e2f1f2", "a2b0b3c3d4e1g0", "a3a5b6c0d1d3f0g1"}, {"a1a4b2c1c2c5e0f0", "a0a5a6b0b1d2g1", "a2a3b3b6d1d3", "a7d0e2e3f2"}, {"a2a4b3b5c1c3c4d1d3f0", "a5b1b2d4e0e1e2e3", "a0b4b6c0c2c5d2f1", "a1a3a6a7b0f2g0"}, {"a1a3a4a7b3b5c1c5f2", "a0a2a6b4c0d2d4f1", "a5b0d0e2e3f0g1", "b2b6c2c3c4d1d3e0e1"}},
            {{"a0a1a4a5a7b2c1c3e0f2g0", "a2a6c4d1d3d4e3f1", "b0b1b4b5d2e1e2f0", "a3b3b6c0c2c5d0g1"}, {"a1a3b6c1c3c4c5d0g1", "a6a7b3c0e0e2g0", "b4b5c2d3d4e3", "a0a2a4a5b0b1e1f2"}, {"a3a6a7c5d3e3f1", "a5b6c2d4f2g0", "b0b1c1c3c4d1e1e2", "a0a4b2b3b4e0f0"}, {"a0a4b1b2d2d3e3f2", "b0b3c1c3f0g0g1", "a1a5b4b5b6e0f1", "a2c0c2c4c5e2"}, {"a0a5a7b1b3c0c2c5d2", "b5b6c1c3d1d4f2g1", "a1a2b4c4d3e0f0", "a3a6b0d0e2f1g0"}},
            {{"a0a7b0c1c5d1d3d4f0", "a6b1b3b4b6c0c3d2e0", "a1a2a5b2b5c4e2", "a3d0e1e3f1f2g0g1"}, {"a5a6b3b5c2d1d3e2e3g0", "b6c0c1c3d4e1f1f2g1", "a0a1a2a3a4a7b2c4e0", "b0b1b4c5d0d2f0"}, {"a5b1c1e0e2e3f0f1", "c2d1d2d3d4e1g0g1", "a1a2a6b4b6c0c4c5", "a3a4b0b2b3b5c3f2"}, {"a0a7c2c4e3f1g1", "a4a5b2b3b5b6e0e1", "a1a3b4c0d0e2g0", "a6b0c1c3c5d2d3d4f2"}, {"a0a5c0c2c5d2d3d4e0f1", "a2a3a4a6b3d0d1f2g0", "b0b1b2b4b6c1c3c4f0", "a1a7b5e1e2e3g1"}},
            {{"a7b0b3b4b5b6e1f0f1f2", "a0a2a4a5a6c0c2d1g1", "a1a3b2c5d2d3e0e2g0", "b1c1c3c4d0d4e3"}, {"a1b0b1b2b4b5b6c2g0", "a0a2a5c5d3d4e3f2", "a3a6a7c3c4d1e2g1", "a4b3c1d2e0e1f0f1"}, {"a3a6b0b3b4d1d4e2f0", "a4a5b1b5c1c2c4d0", "a1b2b6c0d3e0e1g0g1", "a0a2a7c3c5d2e3f1f2"}, {"a0a2a4a6a7b2b4d3e2g0", "a5b5c0c1e0e3f1g1", "a1b0b1b3b6c4c5d0d1d2", "a3c2c3d4e1f0f2"}, {"a0a1a6b0b1b3b6c2e1f0", "c1c3c5d0d2d4", "a5b2b5c0d1g0", "a2a7e3f1f2"}},
    };

    static final char[][] LEGAL_MOVE = {
            {'8', '3', 'W', 'E', '0', 'Z', 'Y'},
            {'S', 'Y', '4', 'G', 'A', 'N', 'O', 'Q', 'R'},
            {'6', 'U', 'O', 'I', 'C', 'Y', '1', '2', '3'},
            {'L', 'E', 'Q', 'W', '2', '8', 'I', 'H', 'G'},
            {'M', 'S', 'Y', '4', 'A', 'H', 'J', 'K', 'L'},
            {'7', 'P', 'J', 'D', 'Z', 'Y', '2', '3'},
            {'B', 'A', 'D', 'E', 'F', 'I', 'O', 'U', '0', '6'},
            {'9', 'R', 'L', 'F', 'V', 'U', 'S'},
            {'N', 'T', 'Z', '5', 'A', 'D', 'E', 'F'},
            {'W', '2', '8', 'K', 'E', 'R', 'P', 'O', 'N', 'M'},
            {'8', '7', '6', '5', '4', '3', 'X', 'R', 'L', 'F'},
            {'3', 'X', 'L', 'F', '7', '6', '4'},
            {'J', 'D', 'V', '1', '7', 'O', 'N', 'M', 'Q', 'R'},
            {'H', 'B', 'T', 'Z', '5', 'M', 'O', 'Q', 'R'},
            {'1', 'P', 'J', 'D', '6', '5', '4', '8', '9'},
            {'W', 'K', 'E', '8', '0', 'Y', '3'},
            {'B', 'N', 'T', 'Z', '5', 'G', 'I', 'J', 'K', 'L'},
            {'J', 'D', '1', '7', 'N', 'M', 'Q', 'R'},
            {'V', '1', '7', 'J', 'D', 'O', 'N', 'M', 'Q', 'R'},
            {'R', 'X', '3', '9', 'F', 'J', 'I', 'H', 'G'}
    };

    static final char[][] ILLEGAL_MOVE_FURTHER = {
            {'1', 'K', 'Q'},
            {'P'},
            {'Z'},
            {'J'},
            {'V', '0'},
            {'I'},
            {},
            {'T', '3'},
            {'H', 'C'},
            {},
            {},
            {'R', '8', '5'},
            {},
            {'P'},
            {'V'},
            {'Q', '1', 'Z'},
            {},
            {'V', 'O'},
            {},
            {'K'}
    };

    static final char[][] ILLEGAL_MOVE_ROW_COL = {
            {'7', 'V', 'X', 'R', 'L', 'O', 'H', '4', 'M', 'A'},
            {'T', 'U', 'J', '1', '3'},
            {'T', 'V', '8', '4', 'K', 'R'},
            {'P', 'X', 'F', 'B', 'Z', '6'},
            {'N', 'B', '0', 'W', 'R'},
            {'W', '8', '6', 'T', 'G', 'F'},
            {'J', 'N', 'Q', '2', '4'},
            {'2', 'P', '0', 'H', 'A'},
            {'I', 'G', 'P', 'Y', '1', '2', 'R'},
            {'X', 'F', '7', 'D', 'U', 'H', 'Y'},
            {'2', 'W', 'U', 'N', 'M', 'D'},
            {'2', '1', 'W', 'P', 'U', 'Z', 'I', 'C', 'A'},
            {'W', 'E', 'C', '6', 'T', 'G', '3'},
            {'S', 'A', 'U', 'J', 'W', '3', '6'},
            {'0', 'W', 'R', 'N', 'G'},
            {'7', '9', 'R', 'P', 'N', 'C', '4'},
            {'A', 'C', 'S', 'V', 'Q', '3', '7'},
            {'I', '0', '2', 'L', 'F', '3', '5'},
            {'I', 'U', '2', 'E', '3', 'Y'},
            {'Q', 'E', 'D', 'O', 'O', 'C', '2', '0', '4', 'S'}
    };

    static final String[] INCOMPLETE_PLACEMENTS = {
            "f0Ca4Db2Fd3Ge1Ha6Ia0Jg1Kd1Mb6Nc0Pa2Ra1Tb1Ub0Vb5We3Xc3Yd41d22c24d05a36z97",
            "f2Bc4Da1Ed2Ge1Ja7Mz9Pc3Ve2We3Xg1Yd31d03a24a48",
            "f1Af0Cb5Ea1Fg1Ge1Hz9Jd1Ld0Mc5Pd4Uc2Vf2Yb11e34c45a07",
            "c3Ae1Cb5Dd0Fg0Ia6Jc4Kc5Mz9Pa0Rd4Se3Tc0Xd20a41b02b64b35",
            "d3Aa7Cf1Gg1Ja3Kz9Mf0Nb4Od4Pb6Qc4Rb3Tf2Zd20c33a54c07a08",
            "f1Az9Bc4De1Ea4Gb4If2Jb5Kd2Mb1Od0Pd3Qa1Sg1Tb6Ud1We2Xc30c11a06e09",
            "d3Dd0Ff1Ga2Kz9Ne0Rc1Sc4Td2Ue3Yb1Zb01g05b38f29",
            "a1Bd4Eb4Fb6Id2Lc4Mg1Nd3Oc5Pz9Sc3Td0Uf1Xb21e12g05a47f29",
            "g1Be0Ce1De3Ef2Fa6Hd1If0Jb2Ke2Md2Na1Ob6Sa7Ub0Vd3Wd4Xz9Zb31a55g06c57c48",
            "z9Ac4Bd4Ce0Hb2Ia1Ne2Qe3Rd2Td1Uf0Vb0Wb5Xg0Ya7Zf11b12a04b65c36b47c08"
    };

    static final char[][] LEGAL_MOVE_INCOMPLETE = {
            {'1', 'V', 'P', 'D', '6', '5', '4'},
            {'V', '1', 'J', 'D', 'M'},
            {'V', '1', '7', 'H', 'G', 'L',},
            {'1', 'J', 'D', 'M', 'R',},
            {'G', 'A', '4', 'N', 'P', 'Q', 'R',},
            {'A', 'D', 'E', 'T'},
            {'T', 'Z', '5', 'R'},
            {'M', 'T', 'U', 'X'},
            {'5', 'N', 'H', 'B', '1'},
            {'B', 'C', 'Y', '4'}
    };

    static final char[][] ILLEGAL_MOVE_NO_CARD = {
            {'8', '9'},
            {'Q', 'R', '7', 'O', 'N'},
            {'I', 'K', 'D'},
            {'Q', 'O', 'N', 'V', '7'},
            {'S', 'Y'},
            {'C', 'F', 'H', 'N', 'Z', '5'},
            {'M', 'O', 'P', 'Q', 'H', 'B'},
            {'V', 'W', 'Y', '4', 'G', 'A'},
            {'0', '2', '3', 'T', 'Y'},
            {'D', 'E', 'F', 'G', 'M', 'S'}
    };

    static final int[][][] FLAGS2 = {
            {{1, 0, 0, 1, 0, 0, 1}, {1, 0, 1, 1, 0, 1, 1}, {1, 0, 0, 1, 1, 0, 1}, {0, 1, 0, 1, 1, 0, 1}, {0, 1, 0, 0, 0, 1, 0}},
            {{1, 0, 0, 0, 1, 1, 1}, {1, 0, 1, 0, 0, 0, 0}, {0, 1, 1, 0, 0, 1, 1}, {0, 0, 1, 1, 1, 0, 1}, {1, 0, 0, 0, 0, 1, 0}},
            {{0, 1, 1, 0, 0, 0, 1}, {1, 1, 0, 1, 0, 1, 0}, {0, 1, 0, 0, 1, 1, 0}, {1, 0, 0, 1, 0, 1, 0}, {1, 0, 1, 0, 0, 1, 1}},
            {{0, 0, 0, 1, 1, 0, 0}, {0, 0, 1, 0, 1, 0, -1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 1, 0}, {1, 0, 1, 0, 1, 1, 0}},
            {{1, 1, 1, 0, 0, 0, 0}, {1, 1, 1, 0, 0, 1, 0}, {0, 1, 0, 1, 0, 0, 0}, {1, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 1, 1, 0, 1}},
            {{1, 0, 1, 1, 0, 0, 0}, {1, 0, 0, 1, 0, 0, 1}, {0, 0, 0, 1, 1, 1, 0}, {0, 0, 1, 1, 0, 0, 0}, {0, 1, 0, 0, 1, 1, 0}},
            {{1, 0, 1, 0, 0, 0, 0}, {1, 0, 0, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 0}, {0, 0, 0, 1, 1, 0, 0}, {0, 1, 1, 1, 0, 0, 1}},
            {{1, 1, 0, 0, 1, 1, 0}, {0, 1, 1, 1, -1, 0, 0}, {0, 1, 1, 0, 1, 1, 0}, {0, 1, 1, 1, 0, 1, 0}, {0, 1, 1, 0, 0, 0, 0}},
            {{1, 0, 1, 0, 1, 1, 1}, {1, 1, 1, 0, 0, 0, 0}, {0, 0, 0, 1, 1, 0, 0}, {1, 1, 0, 0, 0, 1, -1}, {0, 0, 0, 1, 1, 0, 1}},
            {{1, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 1, 1}, {0, 1, 0, 0, 1, 0, 0}, {0, 0, 1, 0, 1, 0, 0}, {0, 0, 1, 1, 1, 0, 0}},
            {{1, 0, 1, 0, 1, 1, 1}, {1, 0, 0, 0, 1, 0, 1}, {1, 1, 0, 0, 0, 1, 1}, {0, 0, 0, 1, 0, 1, 1}, {0, 1, 0, 1, 1, 0, 1}},
            {{0, 0, 1, 1, 0, 1, 0}, {0, 1, 1, 0, 1, 0, 0}, {0, 1, 0, 1, 1, 0, 1}, {1, 1, 0, 1, 1, 0, 0}, {0, 0, 1, 0, 1, 0, 1}},
            {{0, 0, 1, 1, 1, 1, 1}, {0, 0, 1, 1, 0, 1, 1}, {1, 1, 0, 0, 0, 1, 0}, {1, 1, 0, 1, 1, 0, 1}, {0, 0, 1, 0, 1, 0, 1}},
            {{0, 1, 0, 0, 1, 1, 1}, {0, 0, 1, 1, 1, 0, 1}, {1, 1, 0, 0, 1, 0, 0}, {1, 1, 0, 0, 0, 1, 1}, {0, 1, 0, 0, 0, 1, 0}},
            {{0, 0, 0, 1, 0, 0, 1}, {0, 1, 1, 0, 1, 0, 0}, {0, 0, 1, 1, 1, 1, 1}, {1, 1, 0, 1, 0, 0, 0}, {1, 1, 0, 1, 1, 1, 0}},
            {{1, 1, 0, 1, 0, 1, 0}, {0, 0, 0, 0, 0, 1, 1}, {0, 1, 0, 1, 0, 1, 1}, {0, 0, 0, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 1, 1}},
            {{0, 0, 0, 0, 1, 1, 1}, {1, 0, 1, 1, 0, 1, 0}, {0, 0, 0, 1, 1, 0, 1}, {1, 0, 0, 0, 1, 0, 1}, {0, 1, 1, 1, 0, 0, 0}},
            {{0, 0, 1, 1, 0, 0, 0}, {1, 0, 0, 0, 1, 1, 1}, {0, 1, 0, 0, 0, 1, 1}, {0, 0, 1, 0, 0, 0, 1}, {0, 1, 0, 1, 0, 1, 1}},
            {{0, 1, 0, 0, 1, 1, 1}, {0, 1, 1, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 0, 1}, {0, 1, 1, 1, 0, 0, 0}, {1, 0, 0, 0, 1, 0, 1}},
            {{1, 0, 1, 1, 0, 0, 1}, {0, 0, 0, 1, 1, 1, 0}, {1, 0, 1, 0, 0, 1, 0}, {0, 0, 1, 0, 1, 1, 0}, {0, 0, 1, 1, 0, 1, 0}}};
    static final int[][][] FLAGS2_SEVEN_MOVES = {
            {{1, 0, -1, 1, 0, -1, 0}, {0, 0, -1, 1, -1, 0, 1}, {1, 0, 0, 1, -1, -1, -1}, {-1, 1, 0, 0, 1, 1, 0}, {0, 0, 0, 0, -1, 1, 1}},
            {{1, 0, 0, 0, -1, -1, -1}, {0, -1, 1, 1, -1, 0, -1}, {0, 1, 1, 0, -1, -1, 0}, {0, 0, 1, -1, 1, 0, 0}, {-1, -1, 0, 0, 0, 1, -1}},
            {{0, 0, 1, 0, 1, -1, 0}, {1, 1, -1, 0, 0, 0, 0}, {0, -1, 0, 0, 1, -1, -1}, {0, 0, 1, 0, -1, -1, -1}, {1, 0, 0, 1, -1, -1, 1}},
            {{0, -1, 1, -1, 0, 0, -1}, {-1, 0, 0, 1, 1, 0, -1}, {0, -1, 1, 0, 0, -1, 1}, {1, 0, 0, 0, 1, 1, -1}, {-1, 0, 0, 0, 1, -1, -1}},
            {{1, 1, 1, 0, 0, -1, -1}, {1, -1, 0, -1, 0, 1, 0}, {-1, 1, 0, 1, 0, -1, 0}, {1, 0, 0, 0, 0, 1, -1}, {0, -1, 0, 1, 1, -1, -1}},
            {{1, 0, 1, -1, 0, 0, 0}, {-1, 1, 0, 0, 0, -1, 1}, {0, 1, 0, 1, 0, -1, -1}, {0, 0, -1, 1, 0, -1, -1}, {0, -1, -1, 1, 1, -1, 0}},
            {{-1, 0, 1, -1, 0, 1, -1}, {-1, 0, -1, 1, 1, 0, 0}, {1, 0, 1, 0, -1, 1, -1}, {0, 0, 0, 1, -1, -1, -1}, {-1, 1, -1, 0, 1, 0, -1}},
            {{0, -1, 0, -1, 1, 1, -1}, {0, 1, 1, 0, -1, 0, -1}, {1, 1, 0, 0, -1, -1, 0}, {0, 1, -1, 0, -1, -1, -1}, {1, -1, 1, 0, 0, -1, -1}},
            {{1, 0, -1, 0, -1, -1, -1}, {0, 1, -1, 0, -1, 0, -1}, {0, 0, 1, -1, 0, -1, 1}, {1, 0, 0, 1, 0, 1, -1}, {-1, 0, 0, 1, 0, 1, -1}},
            {{1, 0, -1, -1, 0, 1, 0}, {0, -1, 0, -1, 1, -1, -1}, {1, 1, 0, 0, -1, 0, 1}, {0, 1, 0, 0, -1, 0, 1}, {0, -1, 0, 1, 1, -1, 1}},
            {{1, -1, 1, 0, 0, 0, 1}, {0, -1, 0, 0, 1, -1, 1}, {1, 0, 1, 0, -1, 0, -1}, {0, -1, 1, 0, 0, 1, 0}, {1, 1, 0, 1, -1, 0, 0}},
            {{-1, 0, 1, 0, 0, -1, 1}, {-1, 1, -1, 0, 1, 0, -1}, {0, 1, 0, 0, 1, 0, -1}, {-1, 1, 0, 1, 0, 0, -1}, {-1, 0, -1, 1, 1, -1, -1}},
            {{0, 1, 0, -1, 1, 1, 0}, {-1, 1, 0, 0, -1, -1, -1}, {0, 1, -1, 0, 0, -1, 1}, {1, 1, -1, 1, 0, 0, -1}, {0, 1, 1, 0, -1, -1, 1}},
            {{0, 1, -1, 0, -1, 0, -1}, {1, 0, 0, 1, -1, 0, -1}, {-1, 0, 0, 1, -1, -1, -1}, {1, 1, 0, 0, -1, -1, -1}, {1, 0, 1, 0, -1, -1, -1}},
            {{1, 1, -1, -1, 0, 0, -1}, {0, 1, 0, -1, 1, 0, -1}, {0, 0, 1, -1, -1, 1, -1}, {-1, 0, -1, 1, 0, 0, -1}, {-1, 1, 0, 1, 0, 0, -1}},
            {{1, 1, 0, 0, 0, 1, 0}, {0, 1, -1, 0, -1, 1, 0}, {0, 1, -1, -1, 0, 1, -1}, {0, 0, 0, 1, -1, -1, -1}, {1, 0, -1, 1, -1, 1, 0}},
            {{0, 0, 0, 1, 0, 1, -1}, {1, 0, 0, -1, 0, 1, 1}, {0, 0, -1, 1, 0, 0, 1}, {1, 0, 0, -1, 1, -1, -1}, {1, 0, -1, 0, -1, 1, -1}},
            {{0, 0, -1, 1, 0, -1, 1}, {1, 0, 0, 0, 1, -1, 0}, {1, 1, 0, 0, 0, -1, 1}, {0, 0, -1, 0, 0, -1, 1}, {-1, 0, 1, 1, -1, 1, -1}},
            {{0, 1, 0, -1, 1, 0, -1}, {-1, -1, 1, 0, 0, 1, -1}, {-1, -1, 0, 1, 0, 1, 1}, {0, -1, 0, -1, 1, 1, -1}, {1, -1, 0, 1, 0, 1, -1}},
            {{0, -1, 1, 1, -1, 0, 0}, {0, 0, 0, 1, 1, -1, 0}, {-1, 1, 1, 0, 1, 0, -1}, {0, -1, 1, 0, -1, 1, 1}, {0, 0, 1, 0, -1, 1, 0}}};

    static final int[][][] FLAGS3 = {
            {{1, 0, 0, 1, 1, 1, 2}, {1, 0, 1, 2, 2, 2, 0}, {2, 2, 0, 1, 0, 0, 1}, {0, 2, 0, 1, 0, 1, 1}, {1, 0, 2, 0, 2, 0, 2}},
            {{1, 2, 2, 0, 0, 2, 0}, {2, 0, 0, 2, 0, 1, 1}, {1, 1, 0, 0, 0, 1, 1}, {1, 0, 2, 1, 0, 0, 0}, {1, 0, 0, 1, 2, 1, 1}},
            {{1, 1, 0, 2, 2, 0, 0}, {2, 2, 1, 0, 0, 0, 0}, {1, 1, 1, 0, 0, 0, 1}, {1, 2, 1, 1, 0, 0, 1}, {2, 1, 0, 2, 2, 0, 0}},
            {{2, 0, 1, 1, 0, 2, 0}, {2, 0, 1, 1, 0, 2, -1}, {1, 2, 2, 0, 2, 1, 1}, {0, 0, 1, 1, 1, 2, 1}, {0, 0, 1, 1, 0, 1, 2}},
            {{1, 0, 0, 2, 1, 1, 2}, {2, 0, 1, 0, 1, 2, 0}, {0, 0, 0, 2, 2, 1, 2}, {2, 0, 2, 1, 1, 0, 0}, {0, 2, 1, 2, 0, 2, 0}},
            {{2, 0, 1, 0, 2, 1, 0}, {1, 0, 2, 0, 0, 2, 0}, {2, 1, 0, 0, 1, 1, 0}, {1, 2, 2, 0, 1, 0, 2}, {1, 1, 1, 0, 2, 2, 1}},
            {{1, 1, 2, 2, 0, 0, 0}, {1, 2, 2, 2, 0, 0, 1}, {2, 1, 0, 2, 0, 1, 1}, {2, 1, 0, 0, 1, 1, 1}, {2, 0, 1, 2, 2, 0, 2}},
            {{1, 1, 2, 2, 0, 2, 0}, {2, 1, 2, 0, -1, 1, 0}, {2, 0, 2, 2, 1, 2, 1}, {0, 2, 2, 1, 2, 0, 1}, {1, 0, 0, 1, 0, 1, 0}},
            {{2, 1, 2, 0, 0, 2, 0}, {0, 1, 0, 1, 2, 2, 1}, {0, 0, 1, 2, 2, 1, 2}, {1, 1, 1, 0, 1, 1, -1}, {1, 1, 0, 1, 1, 2, 2}},
            {{1, 0, 1, 0, 0, 0, 2}, {2, 1, 1, 1, 2, 0, 1}, {1, 2, 1, 2, 1, 2, 0}, {1, 0, 1, 2, 1, 2, 1}, {0, 2, 1, 0, 0, 2, 1}},
            {{2, 2, 1, 0, 0, 2, 0}, {0, 0, 2, 0, 1, 1, 2}, {2, 2, 2, 1, 0, 0, 1}, {0, 2, 1, 2, 0, 1, 1}, {2, 0, 1, 2, 0, 1, 1}},
            {{2, 2, 0, 1, 0, 1, 1}, {1, 1, 0, 1, 0, 0, 0}, {1, 2, 0, 0, 0, 2, 2}, {0, 1, 2, 2, 1, 0, 0}, {0, 1, 0, 2, 0, 1, 2}},
            {{2, 0, 0, 2, 1, 1, 0}, {0, 1, 2, 0, 1, 1, 2}, {0, 2, 2, 1, 0, 1, 2}, {2, 0, 2, 0, 0, 2, 2}, {2, 1, 0, 2, 0, 1, 2}},
            {{1, 0, 2, 1, 1, 0, 2}, {0, 1, 1, 2, 2, 1, 2}, {2, 2, 1, 1, 1, 1, 1}, {1, 0, 2, 2, 1, 1, 2}, {2, 0, 0, 2, 1, 1, 2}},
            {{2, 1, 0, 1, 0, 2, 1}, {2, 1, 0, 1, 0, 0, 2}, {0, 1, 1, 1, 1, 2, 2}, {0, 2, 0, 0, 0, 2, 2}, {0, 2, 1, 2, 0, 1, 2}},
            {{2, 1, 0, 1, 0, 1, 1}, {0, 2, 0, 1, 0, 1, 1}, {2, 1, 1, 2, 0, 1, 0}, {0, 1, 0, 1, 2, 2, 1}, {2, 2, 2, 1, 0, 1, 1}},
            {{2, 2, 0, 1, 1, 1, 0}, {1, 2, 0, 0, 0, 1, 1}, {0, 0, 2, 2, 0, 1, 1}, {1, 2, 0, 2, 1, 1, 2}, {2, 0, 2, 2, 1, 1, 2}},
            {{2, 0, 0, 2, 1, 1, 2}, {2, 2, 1, 0, 0, 1, 2}, {2, 1, 2, 0, 1, 0, 2}, {2, 1, 1, 2, 0, 0, 2}, {1, 0, 1, 2, 0, 0, 2}},
            {{1, 1, 0, 2, 2, 0, 0}, {2, 1, 1, 0, 2, 2, 0}, {2, 1, 1, 2, 1, 0, 0}, {0, 0, 2, 2, 1, 2, 1}, {0, 2, 0, 2, 1, 2, 1}},
            {{0, 1, 1, 2, 2, 1, 1}, {2, 0, 1, 0, 2, 0, 1}, {2, 0, 2, 0, 0, 1, 0}, {1, 1, 0, 2, 0, 0, 0}, {1, 1, 2, 2, 0, 2, 0}}};
    static final int[][][] FLAGS3_SEVEN_MOVES = {
            {{2, 2, -1, 0, 1, -1, 0}, {2, 0, -1, 2, -1, 1, 0}, {0, 0, 0, 2, -1, -1, -1}, {-1, 2, 0, 2, 0, 1, 1}, {1, 0, 2, 0, -1, 2, 0}},
            {{1, 0, 2, 0, -1, -1, -1}, {0, -1, 0, 1, -1, 1, -1}, {0, 1, 2, 2, -1, -1, 1}, {2, 0, 1, -1, 2, 0, 1}, {-1, -1, 0, 1, 2, 2, -1}},
            {{0, 0, 0, 1, 2, -1, 2}, {0, 2, -1, 2, 1, 0, 0}, {0, -1, 1, 0, 0, -1, -1}, {0, 1, 2, 0, -1, -1, -1}, {2, 0, 0, 1, -1, -1, 0}},
            {{0, -1, 1, -1, 0, 2, -1}, {-1, 1, 0, 1, 2, 2, -1}, {1, -1, 0, 0, 2, -1, 2}, {0, 0, 1, 0, 1, 2, -1}, {-1, 0, 1, 2, 0, -1, -1}},
            {{1, 2, 0, 2, 1, -1, -1}, {0, -1, 2, -1, 0, 2, 0}, {-1, 0, 0, 2, 0, -1, 1}, {2, 0, 2, 1, 0, 1, -1}, {2, -1, 1, 2, 0, -1, -1}},
            {{1, 0, 0, -1, 2, 0, 1}, {-1, 1, 2, 0, 0, -1, 0}, {2, 1, 0, 2, 1, -1, -1}, {1, 2, -1, 0, 0, -1, -1}, {1, -1, -1, 0, 2, -1, 0}},
            {{-1, 0, 2, -1, 2, 1, -1}, {-1, 1, -1, 2, 1, 0, 0}, {2, 2, 0, 1, -1, 1, -1}, {2, 1, 0, 1, -1, -1, -1}, {-1, 0, -1, 0, 2, 2, -1}},
            {{0, -1, 1, -1, 0, 2, -1}, {2, 1, 0, 0, -1, 1, -1}, {2, 0, 0, 2, -1, -1, 1}, {0, 2, -1, 1, -1, -1, -1}, {1, -1, 2, 1, 0, -1, -1}},
            {{2, 1, -1, 0, -1, -1, -1}, {0, 2, -1, 1, -1, 2, -1}, {0, 1, 1, -1, 2, -1, 0}, {1, 0, 0, 0, 1, 2, -1}, {-1, 2, 0, 1, 1, 2, -1}},
            {{2, 0, -1, -1, 2, 1, 1}, {1, -1, 2, -1, 2, -1, -1}, {0, 2, 1, 0, -1, 0, 1}, {1, 0, 0, 2, -1, 0, 2}, {0, -1, 0, 0, 1, -1, 2}},
            {{2, -1, 0, 0, 2, 0, 1}, {0, -1, 2, 0, 1, -1, 0}, {0, 1, 1, 2, -1, 0, -1}, {1, -1, 1, 2, 0, 2, 0}, {2, 0, 0, 1, -1, 1, 0}},
            {{-1, 2, 2, 0, 1, -1, 0}, {-1, 2, -1, 0, 0, 0, -1}, {1, 1, 0, 2, 2, 0, -1}, {-1, 1, 2, 0, 1, 0, -1}, {-1, 0, -1, 2, 0, -1, -1}},
            {{2, 0, 0, -1, 1, 2, 1}, {-1, 1, 2, 0, -1, -1, -1}, {0, 2, -1, 1, 2, -1, 0}, {2, 1, -1, 0, 2, 0, -1}, {1, 0, 1, 0, -1, -1, 2}},
            {{2, 0, -1, 1, -1, 0, -1}, {0, 2, 0, 2, -1, 1, -1}, {-1, 0, 1, 0, -1, -1, -1}, {1, 2, 0, 2, -1, -1, -1}, {2, 0, 1, 0, -1, -1, -1}},
            {{0, 1, -1, -1, 0, 2, -1}, {2, 2, 0, -1, 1, 1, -1}, {0, 1, 0, -1, -1, 2, -1}, {-1, 0, -1, 2, 0, 2, -1}, {-1, 0, 0, 2, 0, 1, -1}},
            {{2, 0, 1, 2, 0, 1, 0}, {2, 0, -1, 1, -1, 1, 0}, {2, 0, -1, -1, 0, 1, -1}, {0, 1, 0, 2, -1, -1, -1}, {2, 0, -1, 0, -1, 1, 2}},
            {{2, 1, 0, 2, 0, 1, -1}, {2, 1, 0, -1, 0, 1, 0}, {2, 0, -1, 2, 0, 1, 1}, {0, 2, 0, -1, 1, -1, -1}, {1, 0, -1, 2, -1, 2, -1}},
            {{1, 0, -1, 1, 2, -1, 0}, {0, 0, 1, 2, 1, -1, 0}, {1, 0, 2, 0, 0, -1, 2}, {2, 0, -1, 0, 1, -1, 2}, {-1, 0, 1, 2, -1, 0, -1}},
            {{0, 2, 1, -1, 0, 0, -1}, {-1, -1, 0, 0, 1, 2, -1}, {-1, -1, 0, 1, 1, 0, 2}, {0, -1, 1, -1, 2, 0, -1}, {0, -1, 0, 1, 0, 2, -1}},
            {{0, -1, 0, 2, -1, 1, 0}, {2, 0, 0, 1, 2, -1, 1}, {-1, 2, 1, 0, 0, 1, -1}, {1, -1, 2, 0, -1, 0, 1}, {0, 1, 2, 2, -1, 0, 0}}};

    static final int[][][] FLAGS4 = {
            {{1, 1, 0, 2, 0, 2, 3}, {1, 2, 3, 1, 0, 1, 3}, {3, 2, 0, 3, 1, 2, 1}, {0, 0, 2, 3, 1, 2, 1}, {0, 3, 2, 0, 0, 1, 2}},
            {{3, 0, 2, 0, 2, 1, 3}, {1, 2, 3, 0, 0, 3, 0}, {0, 3, 1, 2, 3, 2, 1}, {0, 2, 1, 3, 3, 0, 1}, {3, 2, 2, 1, 2, 1, 0}},
            {{3, 0, 1, 2, 0, 2, 1}, {1, 3, 0, 1, 0, 3, 2}, {3, 1, 0, 2, 3, 1, 2}, {0, 1, 2, 3, 0, 3, 0}, {1, 0, 3, 0, 2, 0, 3}},
            {{2, 2, 0, 1, 3, 2, 0}, {0, 0, 3, 1, 0, 2, -1}, {1, 0, 2, 3, 2, 0, 1}, {1, 2, 0, 0, 0, 1, 2}, {3, 0, 1, 1, 1, 3, 2}},
            {{1, 1, 3, 0, 0, 2, 0}, {3, 2, 3, 0, 0, 1, 2}, {0, 3, 3, 1, 0, 2, 0}, {3, 0, 2, 0, 0, 3, 2}, {2, 3, 3, 1, 1, 1, 1}},
            {{3, 3, 3, 1, 0, 0, 0}, {3, 0, 2, 1, 2, 0, 1}, {1, 1, 0, 3, 3, 3, 0}, {0, 0, 3, 1, 2, 0, 2}, {0, 1, 0, 3, 3, 1, 2}},
            {{1, 0, 3, 2, 2, 0, 0}, {1, 2, 3, 1, 1, 3, 3}, {1, 1, 0, 3, 3, 1, 0}, {2, 0, 2, 3, 1, 2, 0}, {2, 2, 0, 1, 0, 0, 1}},
            {{3, 3, 0, 2, 3, 1, 0}, {1, 3, 3, 1, -1, 0, 0}, {2, 3, 1, 2, 3, 3, 2}, {1, 1, 3, 3, 2, 3, 0}, {0, 1, 1, 0, 2, 2, 2}},
            {{3, 0, 1, 0, 1, 1, 3}, {3, 1, 3, 0, 1, 2, 0}, {2, 1, 0, 3, 1, 0, 2}, {1, 3, 0, 0, 0, 1, -1}, {0, 2, 2, 3, 3, 0, 3}},
            {{3, 1, 0, 0, 2, 0, 2}, {2, 0, 3, 1, 0, 1, 3}, {0, 1, 0, 2, 3, 0, 2}, {0, 0, 1, 2, 2, 2, 0}, {2, 0, 3, 3, 3, 0, 0}},
            {{1, 0, 1, 2, 3, 3, 1}, {3, 0, 0, 2, 1, 0, 3}, {1, 3, 0, 2, 2, 1, 1}, {0, 0, 2, 1, 2, 1, 3}, {2, 3, 0, 3, 3, 0, 1}},
            {{0, 3, 1, 1, 0, 3, 2}, {2, 1, 3, 2, 3, 0, 2}, {1, 0, 0, 3, 1, 2, 3}, {3, 1, 2, 1, 3, 0, 2}, {0, 0, 1, 2, 3, 0, 3}},
            {{2, 0, 1, 3, 0, 1, 1}, {2, 2, 1, 3, 0, 1, 3}, {1, 1, 3, 2, 2, 3, 2}, {0, 1, 0, 3, 3, 2, 1}, {2, 0, 1, 0, 1, 2, 1}},
            {{0, 1, 0, 0, 3, 3, 1}, {2, 0, 3, 3, 3, 1, 1}, {1, 1, 0, 2, 3, 2, 2}, {1, 1, 2, 0, 3, 1, 3}, {0, 1, 2, 3, 2, 1, 2}},
            {{2, 2, 0, 3, 0, 2, 1}, {2, 3, 0, 2, 2, 0, 2}, {0, 2, 1, 3, 3, 0, 1}, {1, 3, 2, 3, 0, 0, 2}, {2, 1, 2, 1, 3, 3, 0}},
            {{0, 3, 1, 3, 2, 1, 0}, {2, 3, 0, 2, 0, 1, 1}, {3, 3, 2, 1, 2, 1, 1}, {0, 0, 2, 1, 1, 3, 2}, {0, 0, 3, 3, 1, 3, 1}},
            {{0, 3, 1, 2, 3, 1, 3}, {0, 0, 3, 3, 0, 1, 2}, {1, 2, 0, 2, 3, 0, 1}, {3, 2, 2, 0, 1, 2, 3}, {0, 0, 3, 1, 2, 2, 2}},
            {{0, 2, 3, 1, 2, 0, 0}, {3, 2, 0, 2, 1, 3, 1}, {0, 3, 2, 1, 2, 3, 1}, {0, 2, 3, 0, 2, 1, 1}, {0, 1, 0, 1, 2, 1, 3}},
            {{2, 1, 0, 0, 3, 3, 3}, {2, 3, 1, 3, 0, 1, 0}, {2, 3, 2, 1, 0, 0, 1}, {0, 1, 3, 3, 1, 0, 0}, {1, 2, 0, 0, 3, 0, 1}},
            {{1, 0, 3, 2, 2, 0, 1}, {2, 0, 2, 1, 3, 3, 2}, {3, 0, 1, 0, 2, 3, 2}, {0, 2, 3, 2, 1, 3, 0}, {0, 0, 1, 1, 0, 3, 2}}};
    static final int[][][] FLAGS4_SEVEN_MOVES = {
            {{1, 2, -1, 3, 0, -1, 2}, {2, 2, -1, 1, -1, 0, 3}, {3, 2, 0, 1, -1, -1, -1}, {-1, 1, 2, 2, 3, 1, 0}, {0, 2, 2, 0, -1, 1, 3}},
            {{1, 0, 2, 2, -1, -1, -1}, {2, -1, 3, 1, -1, 0, -1}, {2, 1, 1, 2, -1, -1, 0}, {2, 2, 1, -1, 1, 0, 0}, {-1, -1, 2, 0, 2, 1, -1}},
            {{2, 0, 3, 0, 1, -1, 2}, {3, 1, -1, 2, 0, 0, 2}, {0, -1, 0, 2, 3, -1, -1}, {0, 0, 1, 2, -1, -1, -1}, {1, 2, 0, 1, -1, -1, 3}},
            {{2, -1, 0, -1, 0, 2, -1}, {-1, 0, 2, 1, 1, 2, -1}, {0, -1, 3, 2, 2, -1, 1}, {3, 2, 0, 0, 1, 1, -1}, {-1, 2, 0, 2, 1, -1, -1}},
            {{1, 1, 3, 2, 0, -1, -1}, {3, -1, 2, -1, 0, 1, 2}, {-1, 3, 2, 1, 0, -1, 0}, {1, 2, 2, 0, 0, 1, -1}, {2, -1, 2, 1, 3, -1, -1}},
            {{1, 0, 3, -1, 2, 2, 0}, {-1, 1, 2, 2, 0, -1, 3}, {2, 1, 2, 1, 0, -1, -1}, {0, 2, -1, 3, 2, -1, -1}, {0, -1, -1, 3, 1, -1, 2}},
            {{-1, 0, 1, -1, 2, 1, -1}, {-1, 0, -1, 1, 1, 2, 0}, {1, 2, 3, 0, -1, 1, -1}, {2, 0, 2, 1, -1, -1, -1}, {-1, 3, -1, 0, 1, 2, -1}},
            {{1, -1, 0, -1, 3, 1, -1}, {1, 1, 3, 2, -1, 0, -1}, {1, 3, 2, 2, -1, -1, 0}, {0, 1, -1, 0, -1, -1, -1}, {1, -1, 1, 0, 2, -1, -1}},
            {{1, 0, -1, 2, -1, -1, -1}, {0, 1, -1, 0, -1, 2, -1}, {2, 0, 1, -1, 2, -1, 3}, {1, 2, 0, 3, 0, 1, -1}, {-1, 2, 2, 1, 0, 1, -1}},
            {{1, 2, -1, -1, 2, 1, 0}, {0, -1, 2, -1, 1, -1, -1}, {3, 1, 0, 2, -1, 0, 1}, {0, 3, 2, 2, -1, 0, 1}, {2, -1, 0, 3, 1, -1, 1}},
            {{1, -1, 3, 0, 2, 2, 1}, {2, -1, 2, 0, 1, -1, 3}, {1, 0, 1, 2, -1, 2, -1}, {0, -1, 1, 2, 2, 1, 0}, {1, 3, 2, 1, -1, 0, 0}},
            {{-1, 2, 1, 2, 0, -1, 3}, {-1, 1, -1, 2, 3, 0, -1}, {0, 1, 2, 2, 1, 0, -1}, {-1, 1, 2, 2, 0, 0, -1}, {-1, 2, -1, 1, 3, -1, -1}},
            {{2, 3, 2, -1, 1, 1, 0}, {-1, 1, 2, 2, -1, -1, -1}, {2, 1, -1, 0, 2, -1, 3}, {1, 1, -1, 3, 2, 2, -1}, {0, 3, 1, 2, -1, -1, 1}},
            {{2, 1, -1, 0, -1, 2, -1}, {3, 2, 2, 1, -1, 0, -1}, {-1, 2, 0, 3, -1, -1, -1}, {1, 1, 2, 2, -1, -1, -1}, {1, 0, 1, 2, -1, -1, -1}},
            {{3, 1, -1, -1, 0, 2, -1}, {2, 1, 2, -1, 1, 0, -1}, {2, 0, 3, -1, -1, 1, -1}, {-1, 2, -1, 1, 0, 2, -1}, {-1, 3, 2, 1, 0, 0, -1}},
            {{1, 3, 0, 2, 2, 1, 0}, {2, 3, -1, 0, -1, 1, 0}, {2, 3, -1, -1, 2, 1, -1}, {0, 0, 2, 1, -1, -1, -1}, {1, 2, -1, 3, -1, 1, 2}},
            {{2, 0, 2, 1, 0, 1, -1}, {1, 0, 2, -1, 0, 1, 3}, {2, 2, -1, 1, 0, 0, 1}, {3, 0, 2, -1, 1, -1, -1}, {1, 2, -1, 2, -1, 1, -1}},
            {{0, 2, -1, 1, 2, -1, 3}, {3, 2, 0, 2, 1, -1, 0}, {1, 3, 2, 0, 2, -1, 1}, {2, 2, -1, 0, 0, -1, 1}, {-1, 2, 1, 1, -1, 3, -1}},
            {{2, 1, 0, -1, 3, 0, -1}, {-1, -1, 2, 0, 0, 1, -1}, {-1, -1, 2, 1, 0, 3, 1}, {2, -1, 0, -1, 1, 3, -1}, {3, -1, 2, 1, 0, 1, -1}},
            {{0, -1, 3, 1, -1, 0, 2}, {2, 0, 2, 1, 1, -1, 0}, {-1, 1, 1, 2, 3, 0, -1}, {0, -1, 1, 2, -1, 3, 1}, {0, 0, 1, 2, -1, 3, 2}}};

    static final String[] FINAL_PLACEMENT = {
            "e2Pg0Ce3Dz9Ga09",
            "z91b6U",
            "c1Eb0Gd4Hc0Ib2Ka7Ma5Nf2Pb5Ue3Ve2Wg1Yz99",
            "z96a2Ha4Z",
            "e22z9Ra7Ua3Wb58c4Z",
            "b4Pz95",
            "f2Qc52a7Tf1Wz9Lc2N",
            "c5Cd2Dz94f2Ic3O"
    };

    static String randomCard(Random r) {
        char k = (char) ('a' + r.nextInt(7));
        int n = 8 - (k - 'a');
        char c = (char) ('0' + r.nextInt(n));
        return String.valueOf(k) + c;
    }

    static char randomLocation(Random r) {
        int l = r.nextInt(36);
        if (l < 26)
            return (char) ('A' + l);
        else
            return (char) ('0' + (l - 26));
    }

    static String shufflePlacement(String placement) {
        Random r = new Random();
        int cards = placement.length() / 3;
        if (cards == 1) return placement;

        int order[] = new int[cards];
        for (int i = 1; i < cards; i++) {
            int slot = r.nextInt(cards - 1);
            while (order[slot] != 0) slot = (slot + 1) % cards;
            order[slot] = i;
        }

        String shuffled = "";
        for (int i = 0; i < cards; i++) {
            shuffled += placement.substring(3 * order[i], 3 * (order[i] + 1));
        }
        return shuffled;
    }

    public static String generateRandomSetup() {
        List<String> cards = new ArrayList<>();
        for (char k = 'a'; k < 'h'; k++) {
            for (char c = '0'; c < 8 - (k - 'a') + '0'; c++) {
                cards.add("" + k + c);
            }
        }
        Collections.shuffle(cards);
        StringBuilder sb = new StringBuilder();
        int location = 0;
        for (String card : cards) {
            sb.append(card).append(location < 26 ? location + 'a' : location - 26 + '0');
            location++;
        }
        return sb.toString();
    }

    public static String getCardPlacementBadLocation(Random r) {
        char l = getBadLocation(r);
        return "" + TestUtility.randomCard(r) + l;
    }

    public static char getBadLocation(Random r) {
        char l;
        switch (r.nextInt(3)) {
            case 0:
                l = (char) (r.nextInt('0'));
                break;
            case 1:
                l = (char) (r.nextInt('A' - '9' - 1) + '9' + 1);
                break;
            default:
                l = (char) (r.nextInt(255 - 'Z' - 1) + 'Z' + 1);
        }
        return l;
    }
}
