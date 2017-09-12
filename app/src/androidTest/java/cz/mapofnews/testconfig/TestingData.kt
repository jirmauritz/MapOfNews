package cz.mapofnews.testconfig

import cz.mapofnews.service.Event
import cz.mapofnews.service.EventType
import cz.mapofnews.service.News
import java.util.*
import javax.inject.Singleton

/**
 * This class provide sample data to work with in tests.
 */
@Singleton
class TestingData {

    companion object {
        // sample dates
        val dates by lazy {
            val first = Calendar.getInstance()
            val second = Calendar.getInstance()
            val third = Calendar.getInstance()
            first.set(2017, 9 - 1, 1)
            first.set(2017, 9 - 1, 2)
            first.set(2017, 8 - 1, 18)
            arrayOf(first.time, second.time, third.time)
        }

        // create some testing events
        val events = arrayListOf(
                Event("C0007DD5-59A1-682D-FF0A-7E1DD0ACFD00",
                        EventType.ACCIDENT,
                        dates[0],
                        "Dopravní nehoda osobního vozidla - střet s lesní zvěří.",
                        "Od 2.9.2017 02:30 do 03:40; na silnici 602 u obce Dušejov okres Jihlava; " +
                                "nehoda; zvěř na vozovce; Dopravní nehoda osobního vozidla - střet s lesní zvěří. " +
                                "Místo dopravní nehody průjezdné. Vozidlo po DN odstaveno na krajnici, částečně " +
                                "zasahuje do jízdního pruhu.",
                        "DopravniNehody.cz",
                        49.409136,
                        15.429897),
                Event("E1C10F63-139E-3D08-FF9C-126145A19700",
                        EventType.DISASTER,
                        dates[1],
                        "Potopa ve Strašnicích: V ulici K Rybníčkům prasklo potrubí",
                        "Voda ve Strašnicích v neděli kolem půl čtvré odpoledne zatopila ulici K Rybníčkům a " +
                                "podemlela vozovku. Unikat na povrch začala kvůli prasklému potrubí, které " +
                                "vodohospodáři musí v nejbližších dnech opravit.",
                        "IDNES.cz",
                        50.073531,
                        14.497231),
                Event("01B9F1D4-37D9-2FC0-FF16-BCFAEC9DF700",
                        EventType.CRIME,
                        dates[2],
                        "Devatenáctiletý mladík v Praze nepřežil střet s vlakem",
                        "Na nádraží v pražských Klánovicích srazil v pátek večer vlak devatenáctiletého mladíka. " +
                                "Ten na místě zemřel. Provoz na železničním koridoru mezi Prahou a Kolínem byl přerušen.",
                        "Blesk",
                        50.0867,
                        14.667155)
        )

        val news = hashMapOf(
                Pair("E1C10F63-139E-3D08-FF9C-126145A19700", News("E1C10F63-139E-3D08-FF9C-126145A19700",
                        "Podle agentury Aktu.cz, která vyjela na místo," +
                                " museli policisté ulici K Rybníčkům zcela uzavřít, a to od křižovatky s ulicí Solidarity po " +
                                "křižovatku s ulicí Kolodějská. Ulici K Rybníčkům v " +
                                "Praze zatopila voda, prasklo v ní vodovodní potrubí. Aktu.cz  „Do míst nebyli vpuštěni ani " +
                                "chodci, protože hrozilo větší protržení řadu s možným ohrožením osob.Vodní řad byl uzavřen a " +
                                "dnes nebo zítra začnou výkopové práce za účelem opravy potrubí,” uvedlo Aktu.cz na webových stránkách.")),
                Pair("01B9F1D4-37D9-2FC0-FF16-BCFAEC9DF700", News("01B9F1D4-37D9-2FC0-FF16-BCFAEC9DF700",
                        "Nehoda se stala přímo u perónu klánovického " +
                                "nádraží. Vlak mířil směrem na pražské Hlavní nádraží, když před něj podle svědků skočil " +
                                "mladík z nástupiště.  „Vyšetřujeme střet vlaku s devatenáctiletým mladíkem. Podle prvotního " +
                                "šetření se jednalo o chovance ústavu, který na místě zemřel,” řekla Novinkám policejní mluvčí " +
                                "Andrea Zoulová.  Trať mezi pražským Hlavním nádražím a Kolínem byla na více než hodinu uzavřena. " +
                                "Lidské pozůstatky na místě ohledali kriminalisté s koronerem.  Případem se policie i nadále " +
                                "zabývá. Prověřit musí mimo jiné to, zda se jednalo o sebevraždu, či nešťastnou náhodu."))
        )
    }
}