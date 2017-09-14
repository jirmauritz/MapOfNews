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
        private val dates by lazy {
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
                        null,
                        "Nebrzdi.cz",
                        "http://www.nebrzdi.cz/?id=415050&do=showSingleAccident",
                        49.409136,
                        15.429897,
                        null),
                Event("E1C10F63-139E-3D08-FF9C-126145A19700",
                        EventType.DISASTER,
                        dates[1],
                        "Potopa ve Strašnicích: V ulici K Rybníčkům prasklo potrubí",
                        "Voda ve Strašnicích v neděli kolem půl čtvré odpoledne zatopila ulici K Rybníčkům a " +
                                "podemlela vozovku. Unikat na povrch začala kvůli prasklému potrubí, které " +
                                "vodohospodáři musí v nejbližších dnech opravit.",
                        null,
                        "Blesk.cz",
                        "http://www.blesk.cz/clanek/regiony-praha-praha-zpravy/473416/potopa-ve-strasnicich-v-ulici-k-rybnickum-prasklo-potrubi.html",
                        50.073531,
                        14.497231,
                        "C5D25B95-3131-ECEB-FF3D-BE97CA680C00"),
                Event("01B9F1D4-37D9-2FC0-FF16-BCFAEC9DF700",
                        EventType.CRIME,
                        dates[2],
                        "Devatenáctiletý mladík v Praze nepřežil střet s vlakem",
                        "Na nádraží v pražských Klánovicích srazil v pátek večer vlak devatenáctiletého mladíka. " +
                                "Ten na místě zemřel. Provoz na železničním koridoru mezi Prahou a Kolínem byl přerušen.",
                        null,
                        "Novinky.cz",
                        "https://www.novinky.cz/krimi/448006-devatenactilety-mladik-v-praze-neprezil-stret-s-vlakem.html",
                        50.0867,
                        14.667155,
                        "D4BD00BF-53CE-4138-FF06-B9BEB33F4700")
        )

        val news = hashMapOf(
                Pair("C5D25B95-3131-ECEB-FF3D-BE97CA680C00", News("C5D25B95-3131-ECEB-FF3D-BE97CA680C00",
                        "Podle agentury Aktu.cz, která vyjela na místo," +
                                " museli policisté ulici K Rybníčkům zcela uzavřít, a to od křižovatky s ulicí Solidarity po " +
                                "křižovatku s ulicí Kolodějská. Ulici K Rybníčkům v " +
                                "Praze zatopila voda, prasklo v ní vodovodní potrubí. Aktu.cz  „Do míst nebyli vpuštěni ani " +
                                "chodci, protože hrozilo větší protržení řadu s možným ohrožením osob.Vodní řad byl uzavřen a " +
                                "dnes nebo zítra začnou výkopové práce za účelem opravy potrubí,” uvedlo Aktu.cz na webových stránkách.")),
                Pair("D4BD00BF-53CE-4138-FF06-B9BEB33F4700", News("D4BD00BF-53CE-4138-FF06-B9BEB33F4700",
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