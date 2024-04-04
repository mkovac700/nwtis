<a name="readme-top"></a>

<!-- <div align="right">
<a href="./README.en.md"><img src="https://img.shields.io/badge/%F0%9F%8C%8D%20lang-en-blue?style=flat"></a>
</div> -->

<!-- INTRO -->
<br />
<div align="center">
  
  <img width="96" height="96" src="https://img.icons8.com/fluency/96/airport-building.png" alt="airport-building"/>

  <h3 align="center">NWTiS Projekt</h3>

  <p align="center">
    Složeni sustav za upravljanje aerodromima, udaljenostima i letovima
    <br /> 
    <a href="https://nastava.foi.hr/course/93149"><strong>Saznajte više »</strong></a>
    <br />

  </p>

  <br>
  
  <!-- TABLE OF CONTENTS -->

  <a href="#-uvod">Uvod</a> • 
  <a href="#-opis-projekta">Opis projekta</a> • 
  <a href="#-instalacijska-i-programska-arhitektura-sustava">Instalacijska i programska arhitektura sustava</a> • 
  <a href="#-konfiguracija">Konfiguracija</a> • 
  <a href="#-upotreba">Upotreba</a> • 
  <a href="#-korišteni-alati">Korišteni alati</a>
</div>

<br>

<!--TODO: embed yt video>
<!-- <div align="center">
  <img alt="dz3_demo" src="./dokumentacija/dz3_demo.gif">
</div> -->

<!-- ABOUT THE PROJECT -->
## 📖 Uvod

### Općenito

<table>
  <tbody>
    <tr>
      <td>👦🏽 Autor</td>
      <td>Marijan Kovač</td>
    </tr>
    <tr>
      <td>🧑🏽‍🏫 Nastavnik</td>
      <td>Prof dr. sc. Dragutin Kermek</td>
    </tr>
    <tr>
      <td>📚 Kolegij</td>
      <td>Napredne Web tehnologije i servisi</td>
    </tr>
    <tr>
      <td>🎯 Ocjena</td>
      <td>50/50 (100%)</td>
    </tr>
    <tr>
      <td>🏛️ Ustanova</td>
      <td>Sveučilište u Zagrebu <br> Fakultet organizacije i informatike <br> Varaždin</td>
    </tr>
    <tr>
      <td>📆 Godina <br>polaganja</td>
      <td>2023</td>
    </tr>
  </tbody>
</table>

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>

## 📝 Opis projekta

### mkovac_aplikacija_1

Aplikacija <strong>mkovac_aplikacija_1</strong> predstavlja servis za upravljanje radom cijelog
projekta i za izračunavanje udaljenosti između aerodroma koji koriste ostale aplikacije.
Svoj rad temelji na poslužitelju na mrežnoj utičnici (socket server) na određenim
mrežnim vratima (portu) (postavkom se određuje). Poslužitelj treba biti realiziran kao
višedretveni sustav s ograničenim brojem dretvi (postavkom se određuje). Aplikacija
nema bazu podataka. Aplikacija može primiti komandu KRAJ u bilo kojem načinu rada i
treba prekinuti rad poslužitelja. Aplikacija može primiti komandu STATUS u bilo kojem
načinu rada i treba vratiti trenutni status poslužitelja. Aplikacija je na početku u statusu
pauze i čeka svoju inicijalizaciju koja se provodi komandom INIT. Poslužitelj dok ne primi
komandu INIT odbija ostale standardne komande. Nakon INIT može primiti sve
komande dok ponovno ne pređe u pauzu. Na početku poslužitelj NE ispisuje podatke o
svom radu na standardni izlaz. Komandom INFO odobrava se (DA) ili zabranjuje (NE)
ispis na standardni izlaz. Komande koje treba implementirati:

* STATUS
  * Npr. STATUS
  * Zadatak: vraća trenutni status poslužitelja
  * Korisniku se vraća odgovor OK n. Status n: 0 – pauza, 1 – aktivan
  * Npr. OK 1
* KRAJ
  * Npr. KRAJ
  * Zadatak: prekida prijema novih komandi, čeka na završetak rada aktivnih dretvi i na kraju prekida rad poslužitelja
  * Korisniku se vraća odgovor OK.
  * Npr. OK
* INIT
  * Npr. INIT
  * Zadatak: inicijalizacija poslužitelja i prijelaz u aktivni status, postavljanje brojača obrađenih zahtjeva za izračun udaljenosti na 0
  * Korisniku se vraća odgovor OK
  * Npr. OK
* PAUZA
  * Npr. PAUZA
  * Zadatak: prebacivanje poslužitelja u pauziranje
  * Korisniku se vraća odgovor OK i broj zahtjeva za izračun udaljenosti koje je obradio od zadnjeg prijelaza u aktivni status
  * Npr. OK 27
* INFO [DA|NE]
  * Npr. INFO DA
  * Zadatak: ako je DA time se dopušta ispis primljene komande na standardni izlaz. Ako je NE time se prekida ispis primljene komande na standardni izlaz.
  * Korisniku se vraća odgovor OK
  * Npr. OK
* UDALJENOST gpsSirina1 gosDuzina1 gpsSirina2 gosDuzina2
  * npr. UDALJENOST 46.30771 16.33808 46.02419 15.90968
  * Provjera da li ispravni podaci. Ako su ispravni izračunava udaljenost između dviju lokacija na temelju njihovih GPS pozicija. Algoritam treba uzeti u obzir zakrivljenost Zemlje. Vraća broj km u formatu ddddd.dd. Korisniku se vraća odgovor OK broj. Ako se radi o ispravnom zahtjevu povećava broj zahtjeva za izračun udaljenosti.
  * Npr. OK 271.4

<br>

Kodovi pogrešaka su:

* Kada poslužitelj pauzira a stigla je komanda (PAUZA, INFO, UDALJENOST) vraća odgovor ERROR 01 tekst (tekst objašnjava razlog pogreške) 
* Kada je poslužitelj aktivan a stigla je komanda (INIT) vraća odgovor ERROR 02 tekst (tekst objašnjava razlog pogreške)
* Kada je stigla komanda INFO DA a poslužitelj ispisuje podatke o svom radu na standardni
izlaz vraća odgovor ERROR 03 tekst (tekst objašnjava razlog pogreške)
* Kada je stigla komanda INFO NE a poslužitelj ne ispisuje podatke o svom radu na
standardni izlaz vraća odgovor ERROR 04 tekst (tekst objašnjava razlog pogreške)
* U slučaju bilo koje ostale pogreške vraća odgovor ERROR 05 tekst (tekst objašnjava razlog pogreške)

### mkovac_aplikacija_2

Web aplikacija <strong>mkovac_aplikacija_2</strong> učitava konfiguracijske podatke putem
slušača aplikacije kod pokretanje aplikacije i upisuje ih u atribut konteksta pod nazivom
„konfig“. Naziv datoteke konfiguracijskih podataka zapisan je u web.xml kao inicijalni
parametar konteksta „konfiguracija“ (npr. datoteka NWTiS.db.config_2.xml). Nakon
učitanih konfiguracijskih podataka provjerava status poslužitelja na mrežnoj utičnici iz
{korisnicko_ime}_aplikacija_1. Ako nije aktivan, program prekida rad. Aplikacija pruža
RESTful (JAX-RS) servis pod krajnjom točkom „api“. Svi zahtjevi trebaju se upisivati u
tablicu „DNEVNIK“ putem aplikacijskog filtera (vrsta AP2). Raspoloživi aerodromi nalaze
se u tablici „AIRPORTS“. Udaljenosti između dva aerodroma uz udaljenosti unutar država
nalaze se u tablici „AIRPORTS_DISTANCE_MATRIX“. Postavkama se određuje adresa i
mrežna vrata poslužitelja na mrežnoj utičnici iz mkovac_aplikacija_1.

Sve operacije RESTful web servisa vraćaju odgovor u application/json formatu pomoću klase
Response tako da vraćaju status izvršene operacije i objekt tražene klase kao entitet. POST metode RESTful web servisa šalje podatke u application/json formatu sa strukturom koja je zadana kod pojedine operacije ili je ostavljena na vlastiti izbor.

Potrebno je napraviti sljedeće:

a) RESTful servis koji pruža web aplikacija ima aerodrome kao resurs (putanja „aerodromi“).
Treba implementirati sljedeće operacije:

* get metoda – osnovna putanja, može imati parametre upita pod nazivima traziNaziv,
traziDrzavu, odBroja i broj. Parametar traziNaziv služi za filtriranje aerodroma na
temelju sličnosti naziva aerodroma (SQL upit LIKE %traziNaziv%). Parametar
traziDrzavu služi za filtriranje aerodroma na temelju točne oznake države u kojoj je
aerodrom. Oba parametra mogu biti prazna i tada nema filtriranja. Može jedan biti
prazan i tada se filtriranje provodi po drugom parametru koji nije prazan. Mogu oba
parametra imati vrijednosti i tada oba služe za filtriranje. Vraća podatke za aerodrome
uz straničenje. Ako nisu upisani parametri odBroja i broj tada je odBroja jednak 1, a
broj jednak 20.

* get metoda – putanja {icao}. Varijabilni dio icao je parametar putanje. Vraća podatke
za odabrani aerodrom.

* get metoda – putanja {icaoOd}/{icaoDo}. Varijabilni dio icaoOd i icaoDo su parametri
putanje. Vraća podatke za udaljenosti između odabranih aerodroma unutar država
preko kojih se leti.

* get metoda – putanja {icao}/udaljenosti. Varijabilni dio icao je parametar putanje.
Može imati parametre upita pod nazivima odBroja i broj. Vraća podatke za udaljenosti
između odabranog aerodroma i svih ostalih aerodroma uz straničenje. Ako nisu upisani
parametri tada je odBroja jednak 1, a broj jednak 20.

* get metoda – putanja {icaoOd}/izracunaj/{icaoDo}. Varijabilni dio icaoOd i icaoDo su
parametri putanje. Šalje zahtjev na {korisnicko_ime}_aplikacija_1 s komandom
UDALJENOST gpsSirina1 gosDuzina1 gpsSirina2 gosDuzina2
Podatke za GPS lokacije oba aerodroma preuzima iz tablice „AIRPORTS“. Vraća
podatke za udaljenost između odabranih aerodroma.

* get metoda – putanja {icaoOd}/udaljenost1/{icaoDo}. Varijabilni dio icaoOd i icaoDo
su parametri putanje. Prvo šalje zahtjev na {korisnicko_ime}_aplikacija_1 s komandom
UDALJENOST gpsSirina1 gosDuzina1 gpsSirina2 gosDuzina2
Podatke za GPS lokacije oba aerodroma preuzima iz tablice „AIRPORTS“. S dobivenim podatkom za udaljenost između odabranih aerodroma traže se svi aerodromi koji su u
državi u kojoj je aerodrom icaoDo i čija je udaljenosti manja od dobivene udaljenosti
odredišnog aerodroma. Za udaljenost između dva aerodroma šalje zahtjev na
{korisnicko_ime}_aplikacija_1 s komandom
UDALJENOST gpsSirina1 gosDuzina1 gpsSirina2 gosDuzina2
Podatke za GPS lokacije oba aerodroma preuzima iz tablice „AIRPORTS“. Vraća
podatke za udaljenosti između odabranog aerodroma i svakog aerodroma iz države iz
koje je icaoDo čija je udaljenosti manja od udaljenosti između icaoOd i icaoDo.

* get metoda – putanja {icaoOd }/udaljenost2. Varijabilni dio icaoOd je parametar
putanje. Mora imati parametre upita pod nazivima drzava i km. Vraća aerodrome koji
su u određenoj državi (parametar drzava) i čija je udaljenosti manja od zadane
(parametar km) u odnosu na zadani aerodrom icaoOd. Za udaljenost između dva
aerodroma šalje zahtjev na {korisnicko_ime}_aplikacija_1 s komandom
UDALJENOST gpsSirina1 gosDuzina1 gpsSirina2 gosDuzina2
Podatke za GPS lokacije oba aerodroma preuzima iz tablice „AIRPORTS“. Vraća
podatke za udaljenosti između odabranog aerodroma i svakog aerodroma iz određene
države čija je udaljenosti manja od zadane.

b) RESTful servis koji pruža web aplikacija za upravljanje poslužiteljem na mrežnoj utičnici (putanja „nadzor“). Treba implementirati sljedeće operacije:

* GET metoda - osnovna adresa – šalje komandu STATUS na poslužitelj
{korisnicko_ime}_aplikacija_1. Vraća status 200 i pripadajući JSON sadržaj {status: broj,
opis: tekst} ako je komanda uspješna. Vraća status 400 ako nije uspješna uz tekst
pogreške koji je primljen od poslužitelja.

* GET metoda - putanja "{komanda}" - šalje komandu [KRAJ | INIT | PAUZA] na poslužitelj
{korisnicko_ime}_aplikacija_1. Vraća status 200 i pripadajući JSON sadržaj {status: broj,
opis: tekst} ako je komanda uspješna. Vraća status 400 ako nije uspješna uz tekst
pogreške koji je primljen od poslužitelja.

* GET metoda - putanja "INFO/{vrsta}" - šalje komandu INFO uz vrstu [DA|NE] na
poslužitelj {korisnicko_ime}_aplikacija_1. Vraća status 200 i pripadajući JSON sadržaj
{status: broj, opis: tekst} ako je komanda uspješna. Vraća status 400 ako nije uspješna uz tekst pogreške koji je primljen od poslužitelja.

c) RESTful servis koji pruža web aplikacija za pristup do zapisa u tablici „DNEVNIK“ (putanja
„dnevnik“). Treba implementirati sljedeće operacije:

* get metoda – osnovna putanja, može imati parametre upita pod nazivima vrsta (JAXRS, JAX-WS, UI), odBroja i broj. Parametar vrsta služi za filtriranje aerodroma na
temelju porijekla upisa podataka. Vraća podatke za dnevnik uz straničenje. Ako nisu
upisani parametri tada je odBroja jednak 1, a broj jednak 20.
* post metoda – osnovna putanja. Upisuje zapis u tablicu.

### mkovac_aplikacija_3

Web aplikacija <strong>mkovac_aplikacija_3</strong> učitava konfiguracijske podatke putem
slušača aplikacije kod pokretanje aplikacije i upisuje ih u atribut konteksta pod nazivom
„konfig“. Naziv datoteke konfiguracijskih podataka zapisan je u `web.xml` kao inicijalni
parametar konteksta „konfiguracija“ (npr. datoteka `NWTiS.db.config_3.xml`). Nakon
učitanih konfiguracijskih podataka provjerava status poslužitelja na mrežnoj utičnici iz `mkovac_aplikacija_1`. Ako nije aktivan, program prekida rad. Ako je sve u redu
potrebno je pokrenuti dretvu. Kod zaustavljanja aplikacije potrebno je zaustaviti dretvu.
Dretva radi u ciklusima identičnog trajanja (dretva ima svoj radni dio u ciklusu, a spava
preostalo vrijeme trajanja ciklusa). Zadano je trajanje za ciklus (postavka „ciklus.trajanje“, u sekundama). Dretva ima zadatak da u svakom ciklusu preuzme polaske aviona na
određeni dan za skup aerodroma koji je određen tablicom `AERODROMI_LETOVI`. Na
kraju svakog ciklusa šalje se JMS poruka s TextMessage sadržajem "Na dan: dd.mm.gggg
preuzeto ukupno x letova aviona". JMS poruka šalje se u red poruka
`jms/NWTiS_mkovac`. Početni dan je određen postavkom `preuzimanje.od`, a
završni dan je određen postavkom `preuzimanje.do`. U svakom ciklusu dan se povećava
za 1. Dretva na početku provjerava zadnji zapis u tablici kako bi se utvrdio za koji dan je bilo zadnje preuzimanje. Ako je zadnji dan veći od dana iz postavke `preuzimanje.od `tada je zadnji dan uvećan za jedan dan s kojim se nastavlja rad. Ako je zadnji dan manji od dana iz postavke `preuzimanje.od` tada je dan iz postavke `preuzimanje.od` s kojim se
nastavlja rad. Podaci o polascima letova upisuju se u tablicu `LETOVI_POLASCI`. Za
preuzimanje podataka o polascima letova s aerodroma koristi se Java biblioteka
`nwtis_rest_lib v 3.0.0` koja se nalazi u repozitoriju
http://nwtis.foi.hr:8088/repository/nwtis_2023/1. Biblioteka se temelji na RESTful servisu
koji pruža OpenSky Network te predstavlja njegov omotač. JavaDoc biblioteke nalazi se
na adresi https://nwtis.foi.hr/NWTiS/apidocs/nwtis_rest_lib/3.0.0/2. Za korištenje
OpenSky Network i `nwtis_rest_lib` potrebno je obaviti registraciju kako bi se dobili
korisnički podaci i spremili u konfiguracijsku datoteku (postavke
„OpenSkyNetwork.korisnik“ i „OpenSkyNetwork.lozinka“).

### mkovac_aplikacija_4

Web aplikacija <strong>mkovac_aplikacija_4</strong> učitava konfiguracijske podatke putem
slušača aplikacije kod pokretanje aplikacije i upisuje ih u atribut konteksta pod nazivom
„konfig“. Naziv datoteke konfiguracijskih podataka zapisan je u `web.xml` kao inicijalni
parametar konteksta „konfiguracija“ (npr. datoteka `NWTiS.db.config_4.xml`). Nakon
učitanih konfiguracijskih podataka provjerava status poslužitelja na mrežnoj utičnici iz
`mkovac_aplikacija_1`. Ako nije aktivan, program prekida rad. Ako je sve u redu
nastavlja s radom. Ova aplikacija pruža JAX-WS (SOAP) servise na krajnjim točkama
„korisnici“, „aerodromi“, „letovi“ i „meteo“ te pruža WebSocket krajnju točku „info“. Ova
aplikacija koristi bazu podataka. Podaci o korisnicima nalaze se u tablici "`KORISNICI`". Svi
zahtjevi trebaju se upisivati u tablicu "`DNEVNIK`" putem aplikacijskog filtera (vrsta AP4).
Svi zahtjevi JAX-WS servisa (osim dodavanja korisnika) moraju proći autentikaciju na
bazi korisničkog imena i lozinke. Ako nije uspješna okida vlastitu iznimku pod nazivom
`PogresnaAutentikacija(tekst)`. Potrebno se držati zadanih osobina i realizirati sljedeće
metode:

a) JAX-WS krajnja točka „korisnici“:

* `List<Korisnik> dajKorisnike(String korisnik, String lozinka, String
traziImeKorisnika, String traziPrezimeKorisnika)` – vraća kolekciju korisnika koji
zadovoljavaju filtere ime i prezime korisnika po principu sličnosti (SQL upit
LIKE %traziImeKorisnika% odnosno LIKE %traziPrezimeKorisnika%). Oba
parametra mogu biti prazna i tada nema filtriranja. Može jedan biti prazan i
tada se filtriranje provodi po drugom parametru koji nije prazan. Mogu oba
parametra imati vrijednosti i tada oba služe za filtriranje.

* `Korisnik dajKorisnika(String korisnik, String lozinka, String traziKorisnika)` –
vraća korisnika koji zadovoljava korisničko ime.

* `boolean dodajKorisnika(Korisnik korisnik)` – dodaje korisnika. Nakon
dodavanja šalje obavijest putem WebSocket-a o ukupnom broju upisanih
korisnika.

b) JAX-WS krajnja točka „aerodromi“:

* `List<Aerodrom> dajAerodromeZaLetove(String korisnik, String lozinka)` –
vraća kolekciju aerodroma za koje su preuzimaju podaci o polascima (tablica
AERODROMI_LETOVI).

* `boolean dodajAerodromZaLetove (String korisnik, String lozinka, String icao)`
– dodaje aerodrom s icao kako bi se za njega preuzimali podaci o polascima
(tablica AERODROMI_LETOVI). Nakon dodavanja šalje obavijest putem
WebSocket-a o ukupnom broju upisanih aerodroma.

* `boolean pauzirajAerodromZaLetove (String korisnik, String lozinka, String
icao)` – postavlja status aerodroma s icao da je preuzimanje u pauzi i da se ne
preuzimaju podaci o polascima (tablica AERODROMI_LETOVI).

* `boolean aktivirajAerodromZaLetove (String korisnik, String lozinka, String
icao)` – postavlja status aerodroma s icao da se za njega ponovno preuzimaju
podaci o polascima (tablica AERODROMI_LETOVI).

c) JAX-WS krajnja točka „letovi“:

* `List<LetAviona> dajPolaskeInterval(String korisnik, String lozinka, String icao,
String danOd, String danDo, int odBroja, int broj)` – vraća kolekciju letova koji
su poletjeli s aerodroma koji ima traženi icao u zadanom intervalu.
vrijemeOd/Do je u formatu dd.mm.gggg. Parametri odBroja i broj služe za
straničenje. Za letove podaci trebaju odgovarati klasi LetAviona. Podaci se
preuzimaju iz tablice LETOVI_POLASCI koje je aplikacija
{korisnicko_ime}_aplikacija_3 preuzela s OpenSkyNetwork.

* `List<LetAviona> dajPolaskeNaDan(String korisnik, String lozinka, String icao,
String dan, int odBroja, int broj)` – vraća kolekciju letova koji su poletjeli s
aerodroma koji ima traženi icao na određeni dan, koji je u formatu
dd.mm.gggg. Parametri odBroja i broj služe za straničenje. Za letove podaci
trebaju odgovarati klasi LetAviona. Podaci se preuzimaju iz tablice
LETOVI_POLASCI koje je aplikacija {korisnicko_ime}_aplikacija_3 preuzela s
OpenSkyNetwork.

* `List<LetAviona> dajPolaskeNaDanOS(String korisnik, String lozinka, String
icao, String dan)` – vraća kolekciju letova koji su poletjeli s aerodroma koji ima
traženi icao na određeni dan, koji je u formatu dd.mm.gggg. Za letove podaci
trebaju odgovarati klasi LetAviona. Podaci se preuzimaju iz OpenSkyNetwork.
Za preuzimanje podataka o polascima letova s aerodroma koristi se Java
biblioteka nwtis_rest_lib v 3.0.0.

d) JAX-WS krajnja točka „meteo“:

* `MeteoPodaci dajMeteo(String icao)` - dohvaća meteorološke podatke za
zadani aerodrom iz primljenog argumenta. Kako bi se dobila GPS lokacija
aerodroma šalje se GET zahtjev RESTful web servisu iz
mkovac_aplikacija_2 na resursu „aerodromi“. S lokacijom aerodroma
poziva se metoda getRealTimeWeather(lokacija) na objektu OWMKlijent.

e) WebSocket krajnja točka „info“:

* `dajMeteo(info)` - obavještavanje svojih aktivnih korisnika o trenutnom
vremenu na poslužitelju aplikacije, broju korisnika i broju aerodroma za koje
se prikupljaju podaci o polascima.

### mkovac_aplikacija_5

Web aplikacija <strong>mkovac_aplikacija_5</strong> učitava konfiguracijske podatke putem
slušača aplikacije kod pokretanje aplikacije i upisuje ih u atribut konteksta pod nazivom
„konfig“. Naziv datoteke konfiguracijskih podataka zapisan je u `web.xml` kao inicijalni
parametar konteksta „konfiguracija“ (npr. datoteka `NWTiS.db.config_5.xml`). Nakon
učitanih konfiguracijskih podataka provjerava status poslužitelja na mrežnoj utičnici iz
`mkovac_aplikacija_1`. Ako nije aktivan, program prekida rad. Ako je sve u redu
nastavlja s radom. Aplikacija se bavi administrativnim poslovima kroz korisničko sučelje
realizirano s Jakarta MVC. Ova aplikacija nema vlastitu bazu podataka. Svi zahtjevi
trebaju se upisivati u tablicu „DNEVNIK“ putem aplikacijskog filtera (vrsta AP5) tako da
se šalje REST zahtjev POST metodom na `mkovac_aplikacija_2`. Svaki obrazac u
bilo kojem pogledu treba POST metodom slati podatke. Svoj rad temelji na slanju
zahtjeva na JAX-WS web servis iz `mkovac_aplikacija_4` i slanju zahtjeva na
RESTful/JAX-RS web servis iz `mkovac_aplikacija_2`. Aplikacija sadrži potrebne
Singleton Session Bean (SiSB), Stateful Sesssion Bean (SfSB), Stateless Sesssion Bean
(SlSB), Message-Driven Bean. Aplikacija preuzima JMS poruke iz reda poruka
„`jms/NWTiS_mkovac`“ koje su poslane kod završetka pojedinog ciklusa dretve u
`mkovac_aplikacija_3`. Primljene JMS poruke spremaju se u kolekciju u SiSB.
Postoji mogućnost pražnjenja svih primljenih/spremljenih JMS poruka. Potrebno se
držati zadanih osobina i realizirati sljedeće dijelove:

* pogled 5.1:
  * početni izbornik koji sadrži poveznice na ostale izbornike s predloškom 5.? (5.2, 5.3, 5.4, 5.5, 5.6 i 5.7)

* pogled 5.2:
  * početni izbornik koji sadrži poveznice na ostale aktivnosti vezane uz korisnike
  * pogled 5.2.1:
    * registraciju korisnika. Šalje se zahtjev JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4.
  * pogled 5.2.2:
    * prijavljivanje korisnika. Šalje se zahtjev JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4.
  * pogled 5.2.3:
    * pregled korisnika uz filtiranje po imenu i prezimenu. Korisnici se dobiju slanjem zahtjeva JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4. Putem WebSocket poruke i JavaScript koda ažurira se ukupan broj upisanih korisnika.

* pogled 5.3:
  * upravljanje poslužiteljem iz {korisnicko_ime}_aplikacija_1. Za svaku komandu (STATUS, KRAJ, INIT, PAUZA, INFO DA, INFO NE) postoji gumb koji pokreće akciju. Šalje se zahtjev JAX-RS web servisu iz {korisnicko_ime}_aplikacija_2. Interpretiraju se i zatim ispisuju primljeni podaci.

* pogled 5.4:
  * pregled svih primljenih JMS poruka (straničenje) iz reda čekanja „jms/NWTiS_{korisnicko_ime}“ koji su spremljeni u kolekciji SiSB. Postoji gumb „Obriši“ kojim se pokreće brisanje svih primljenih/spremljenih JMS poruka i osvježava se pregled JMS poruka.

* pogled 5.5:
  * početni izbornik koji sadrži poveznice na ostale aktivnosti vezane uz
  aerodrome

  * pogled 5.5.1:
    * pregled svih aerodroma uz filtriranje po nazivu aerodroma i državi, uz
    straničenje. Šalje se zahtjev JAX-RS web servisu iz
    {korisnicko_ime}_aplikacija_2. Osim podataka uz pojedini aerodrom
    stavlja se poveznica putem koje se aerodrom dodaje za preuzimanje
    podataka o polascima. Šalje se zahtjev JAX-WS web servisu iz
    {korisnicko_ime}_aplikacija_4. Putem WebSocket poruke i JavaScript
    koda ažurira se ukupan broj upisanih aerodroma za preuzimanje.
  * pogled 5.5.2:
    * pregled podataka izabranog aerodroma na temelju poveznice iz
    pogleda 5.5.1. Šalje se zahtjev JAX-RS web servisu iz
    {korisnicko_ime}_aplikacija_2. Osim podataka o aerodromu prikazuju
    se trenutni meteorološki podaci za njegovu lokaciju. Šalje se zahtjev
    JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4.
  * pogled 5.5.3:
    * pregled aerodroma za koje se za preuzimaju podaci o polascima. Šalje
    se zahtjev JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4. Uz
    pojedini aerodrom prikazuje se status preuzimanja podataka
    (Da/Pauza) te se stavlja poveznica putem koje se mijenja status
    preuzimanja za aerodrom. Šalje se zahtjev JAX-WS web servisu iz
    {korisnicko_ime}_aplikacija_4. Putem WebSocket poruke i JavaScript
    koda ažurira se ukupan broj upisanih aerodroma za preuzimanje.
  * pogled 5.5.4:
    * pregled udaljenosti između dva aerodroma unutar država preko kojih
    se leti te ukupna udaljenost. Unose se dva icao podatka. Šalje se zahtjev
    JAX-RS web servisu iz {korisnicko_ime}_aplikacija_2. Sljedeći podatak je
    izračunata udaljenost između dva aerodroma dobivena zbrojem
    udaljenosti unutar država.
  * pogled 5.5.5:
    * pregled udaljenosti između dva aerodroma. Unose se dva icao
    podatka. Šalje se zahtjev JAX-RS web servisu iz
    {korisnicko_ime}_aplikacija_2 koji za izračun udaljenosti koristi komandu
    UDALJENOST u {korisnicko_ime}_aplikacija_1.
  * pogled 5.5.6:
    * pregled aerodroma i udaljenosti do polaznog aerodroma unutar
    države odredišnog aerodroma koji su manje udaljeni od udaljenosti
    između polaznog i odredišnog aerodroma. Unose se dva icao podatka.
    Šalje se zahtjev JAX-RS web servisu iz {korisnicko_ime}_aplikacija_2 za
    izračun udaljenosti koji koristi komandu UDALJENOST u
    {korisnicko_ime}_aplikacija_1.
  * pogled 5.5.7:
    * pregled aerodroma i udaljenosti do polaznog aerodroma unutar
    zadane države koje su manje od zadane udaljenosti. Unose se icao,
    oznaka države i broj km. Šalje se zahtjev JAX-RS web servisu iz
    {korisnicko_ime}_aplikacija_2 za izračun udaljenosti koji koristi komandu
    UDALJENOST u {korisnicko_ime}_aplikacija_1.

* pogled 5.6:
  * početni izbornik koji sadrži poveznice na ostale aktivnosti vezane uz letove
  * pogled 5.6.1:
    * pregled spremljenih letova s određenog aerodroma u zadanom
    intervalu, uz straničenje. Unose se icao, datum od i datum do. Datumi
    su u formatu dd.mm.gggg. Šalje se zahtjev JAX-WS web servisu iz
    {korisnicko_ime}_aplikacija_4.
  * pogled 5.6.2:
    * pregled spremljenih letova s određenog aerodroma na zadani datum,
    uz straničenje. Unose se icao, datum. Datum je u formatu dd.mm.gggg.
    Šalje se zahtjev JAX-WS web servisu iz {korisnicko_ime}_aplikacija_4.
  * pogled 5.6.3:
    * pregled letova s određenog aerodroma na zadani datum. Unose se
    icao, datum. Datum je u formatu dd.mm.gggg. Šalje se zahtjev JAX-WS
    web servisu iz {korisnicko_ime}_aplikacija_4 koji preuzima podatke iz
    OpenSkyNetwork

* pogled 5.7:
  * pregled zapisa dnevnika (straničenje). Unosi/odabere se vrsta (AP2, AP4, AP5).

<br> <br>

Za više detalja kliknite <a href="../dokumentacija/projekt/NWTiS_2022_2023_v1.0.pdf"><strong>ovdje</strong></a>.

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>

## 🧩 Instalacijska i programska arhitektura sustava

<table>
  <thead>
    <tr>
      <th>Aplikacija</th>
      <th>Razvojni alat</th>
      <th>Java</th>
      <th>Poslužitelj</th>
      <th>EE osobine</th>
      <th>Korisničko sučelje</th>
      <th>Baza podataka</th>
      <th>Rad s bazom podataka</th>
      <th>Namjena</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1</td>
      <td>Eclipse s Maven</td>
      <td>17</td>
      <td>Vlastiti</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td>Poslužitelj na mrežnoj utičnici (socket server)</td>
    </tr>
    <tr>
      <td>2</td>
      <td>Eclipse s Maven</td>
      <td>17</td>
      <td>Docker/Payara Micro</td>
      <td>Jakarta EE10 Web</td>
      <td></td>
      <td>Docker/JRE <br> HSQLDB <br> nwtis_bp</td>
      <td>JPA Criteria API</td>
      <td>RESTful/JAX-RS web servis</td>
    </tr>
    <tr>
      <td>3</td>
      <td>Eclipse s Maven</td>
      <td>17</td>
      <td>Payara Full</td>
      <td>Jakarta EE10 Web</td>
      <td></td>
      <td>Docker/JRE <br> HSQLDB <br> nwtis_bp</td>
      <td>JPA Criteria API</td>
      <td>Preuzima podatke o polascima aviona s izabranih aerodroma</td>
    </tr>
    <tr>
      <td>4</td>
      <td>Eclipse s Maven</td>
      <td>17</td>
      <td>Payara Full</td>
      <td>Jakarta EE10</td>
      <td></td>
      <td>Docker/JRE <br> HSQLDB <br> nwtis_bp</td>
      <td>JPA Criteria API</td>
      <td>JAX-WS web servis <br> WebSocket krajnja točka</td>
    </tr>
    <tr>
      <td>5</td>
      <td>Eclipse s Maven</td>
      <td>17</td>
      <td>Payara Full</td>
      <td>Jakarta EE10</td>
      <td>Jakarta MVC</td>
      <td></td>
      <td></td>
      <td>Pogledi za rad s: <br> • korisnicima <br> • primljenim JMS porukama  <br> • aerodromima <br>• letovima</td>
    </tr>
    
  </tbody>
</table>

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>

## ⚙️ Konfiguracija

### Preduvjeti

Za pokretanje rješenja bit će vam potrebno sljedeće:

* Operativni sustav Linux

  Za izradu rješenja korišten je <a href="https://linuxmint.com/">Linux Mint</a> na <a href="https://www.virtualbox.org/">Oracle VirtualBox VM</a>. 

* Java 17 ili veća:
  * smjestite se u željeni direktorij za preuzimanja, npr:
  ```bash
  cd /opt/alati/
  ```

  * preuzmite Java OpenJDK 17.0.2:
  ```bash
  curl https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz -O
  ```

  * smjestite se u slijedeći direktorij (preporuka):
  ```bash
  cd /usr/lib/jvm
  ```

  * izdvojite arhivu:
  ```bash
  sudo tar -xvf /opt/alati/openjdk-17.0.2_linux-x64_bin.tar.gz -C .
  ```

  * podesite varijablu okruženja:

  Otvorite datoteku `/etc/profile.d/jdk.sh`

  i promijenite/dodajte slijedeću liniju:

  ```bash
  export JAVA_HOME=/usr/lib/jvm/jdk-17.0.2
  ```

  * restartirajte sustav (preporuka)

  * provjerite ispravnost instalacije:

  ```bash
  java -version
  ```

  Ako je sve bilo u redu, trebali biste dobiti rezultat sličan ovome:

  ```bash
  openjdk version "17.0.2" 2022-01-18
  OpenJDK Runtime Environment (build 17.0.2+8-86)
  OpenJDK 64-Bit Server VM (build 17.0.2+8-86, mixed mode, sharing)
  ```

* Eclipse IDE

  Preuzmite i instalirajte <a href="https://www.eclipse.org/ide/">Eclipse IDE</a>. Konfigurirajte Eclipse IDE za Java 17 prema potrebi.

* DBeaver (opcionalno)

  👉🏽 DBeaver je dodatak za Eclipse IDE koji olakšava rad s bazama podataka.

  * Help > Eclipse Marketplace > Find

    ⚠️ Nakon provedene instalacije potrebno je ponovno pokrenuti Eclipse IDE

* Maven (opcionalno):

  ⚠️ Maven bi trebao raditi unutar Eclipse IDE bez instalacije, ali po potrebi se može i ručno instalirati za rad putem konzole.

  * ažurirajte repozitorij:
  ```bash
  sudo apt update
  ```
  * instalirajte Maven:
  ```bash
  sudo apt install maven
  ```
  * provjerite ispravnost instalacije:
  ```bash
  mvn -version
  ```
  Ako je sve bilo u redu, trebali biste dobiti rezultat sličan ovome:
  ```bash
  Apache Maven 3.6.3
  Maven home: /usr/share/maven
  Java version: 17.0.2, vendor: Oracle Corporation, runtime: /usr/lib/jvm/jdk-17.0.2
  Default locale: en_US, platform encoding: UTF-8
  OS name: "linux", version: "5.15.0-69-generic", arch: "amd64", family: "unix"
  ```

* Docker

  * Podesite Docker-ov `apt` repozitorij:

  ```bash
  # Add Docker's official GPG key:
  sudo apt-get update
  sudo apt-get install ca-certificates curl
  sudo install -m 0755 -d /etc/apt/keyrings
  sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
  sudo chmod a+r /etc/apt/keyrings/docker.asc

  # Add the repository to Apt sources:
  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
    $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  sudo apt-get update
  ```

  ⚠️ Ako koristite distribuciju utemeljenu na Ubuntu, npr. Linux Mint, možda ćete morati koristiti `UBUNTU_CODENAME` umjesto `VERSION_CODENAME`.

  * Instalirajte Docker:

  ```bash
  sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
  ```

  * Provjerite ispravnost instalacije:

  ```bash
  sudo docker run hello-world
  ```

  Za više informacija kliknite <a href="https://docs.docker.com/engine/install/ubuntu/"><strong>ovdje</strong></a>.

* Payara

  👉🏽 Potrebno je instalirati Payara Web i Payara Full inačice. U nastavku su dane upute za Payara Full.

  * Preuzmite Payara Server 6.2023.4 (Full/Web Profile) na sljedećoj <a href="https://www.payara.fish/downloads/payara-platform-community-edition/"><strong>poveznici</strong></a>.

    ⚠️ Za projekt je korištena verzija 6.2023.4, međutim ista možda više nije dostupna, pa je potrebno preuzeti najnoviju verziju. To znači da naredbe u nastavku treba prilagoditi kako bi iste funkcionirale.

  * Kopirati datoteku s `Downloads` na `/opt/alati`

  * Smjestiti se u direktorij `/opt`
    
    ```bash
    cd /opt
    ```

  * Izvršiti naredbu

    ```bash
    sudo unzip alati/payara-6.2023.4.zip
    mv payara6/ payara-full-6.2023.4
    ```

  * Podesiti prava pristupa ⚠️

    ```bash
    sudo chown -R {admin} payara-full-6.2023.4
    sudo chgrp -R users payara-full-6.2023.4
    sudo chmod -R g+w payara-full-6.2023.4
    ```

* HSQLDB

  * Preuzmite HSQLDB na sljedećoj <a href="https://hsqldb.org/"><strong>poveznici</strong></a>

    ⚠️ Za projekt je korištena verzija 2.7.1

  * Izdvojite preuzetu mapu na željenu destinaciju

    Primjer:

    ```bash
    /opt/hsqldb-2.7.1/hsqldb
    ```

* Postman

  * Preuzmite Postman na sljedećoj <a href="https://www.postman.com/downloads/"><strong>poveznici</strong></a>

  * Alternativno se može koristiti naredba (Linux)

    ```bash
    curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | sh
    ```

* Soap UI

  * Preuzmite Soap UI (Open Source) na sljedećoj <a href="https://www.soapui.org/downloads/soapui/"><strong>poveznici</strong></a>  

  * Pokrenite preuzetu `.sh` skriptu te pratite daljnje korake

### Priprema

  * Klonirajte ovaj repozitorij:

    ```bash
    https://github.com/mkovac700/nwtis.git
    ```

  * Otvorite projekt u Eclipse IDE:

    * `File > Open Projects from File System... > Directory...` 

    * Odaberite korijenski direktorij projekta, a zatim iz popisa odaberite sve direktorije (uključujući i korijenski direktorij) (`Select All`)
  
  * Uvezite postavke konfiguracija za buildanje aplikacija u Eclipse IDE (preporuka):

    * `File > Import... > Run/Debug > Launch Configurations > Browse...`

    * Konfiguracije se nalaze u direktoriju:

      ```bash
      nwtis/konfiguracije
      ```

  * Podesite bazu podataka:

    * Inicijalno pokretanje i kreiranje baze podataka:

      * Smjestite se u direktorij gdje je instaliran HSQLDB, primjerice:

        ```bash
        cd /opt/hsqldb-2.7.1/hsqldb
        ```

        te pokrenite bazu koristeći

        ```bash
        ./hsqldb-server.sh
        ```

      * U drugom terminalu izvršite sljedeću naredbu:

        ```bash
        ./sqltool.sh localhost:9001
        ```

        👉🏽 Navedena će naredba otvoriti SQL terminal

      * Kreirajte novu bazu:

        ```bash
        CREATE DATABASE nwtis_2;
        ```

        ⚠️ Važno je da naziv baze bude isti kako bi sve bilo kompatibilno sa postojećim skriptama i konfiguracijskim datotekama

      * Sada se može ugasiti terminal koristeći naredbu

        ```bash
        \q
        ```

      * Nakon toga je potrebno ugasiti bazu terminiranjem prvog terminala u kojem je baza pokrenuta (`CTRL+Q`)

    * Povezivanje na bazu `nwtis_2`:

      * Otvorite novi terminal i izvršite naredbu:

      ```bash
      cd /opt/hsqldb-2.7.1/hsqldb/data
      sudo java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server \
      --database.0 file:nwtis_2 --dbname.0 nwtis_2 --port 9001
      ```

    * Povezivanje na bazu unutar Eclipse IDE:

      * Pokrenite Eclipse IDE i otvorite perspektivu DBeaver

      * Kreirajte novu vezu `Database Navigator > Desni klik > Create > Connection` i unesite podatke kao na slici:

        <div align="center">
          <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_13.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_13.png"></a>
        </div>

        ⚠️ Prvi put će Eclipse IDE javiti grešku da ne postoji driver, no trebao bi automatski ponuditi instalaciju istog

        👉🏽 Koristite `Test Connection...` za provjeru spajanja na bazu

      * Izvršite skriptu `nwtis/mkovac-projekt/Scripts/ADD_USER.sql`

      * Kreirajte još jednu vezu koristeći `Database Navigator > Desni klik > Create > Connection`, ovaj put s podacima kao na slici:

        <div align="center">
          <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_14.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_14.png"></a>
        </div>

        👉🏽 Korisničko ime i lozinka odgovaraju onoj u skripti `ADD_USER.sql`

        👉🏽 Koristite `Test Connection...` za provjeru spajanja na bazu

    * Kreiranje DDL:

      * Najprije izvršite skriptu `nwtis/mkovac-projekt/Scripts/Tablice_DDL.sql`

      * Potom se mogu izvršiti preostale skripte:

        * `Dnevnik.sql`
        * `Korisnici.sql`
        * `Aerodromi_Letovi.sql`
        * `Letovi_Polasci.sql`

        ⚠️ VAŽNO: U danim skriptama pripazite da ne izvršite i naredbe `DROP`, `DELETE` i slično. Redoslijed izvršavanja ovdje nije bitan.

    * Uvoz potrebnih podataka:

      Izvršite skripte:
    
      * `AIRPORTS_Podaci.sql`
      * `AIRPORTS_DISTANCE_MATRIX_Podaci.sql`
      
      ⚠️ VAŽNO: Bitan je navedeni redoslijed izvršavanja skripti.

  * Podesite JMS poruke (samo za Payara Full ⚠️):

    * Smjestite se u sljedeći direktorij (mjesto gdje je instaliran Payara Full) i pokrenite `asadmin`:

    ```bash
    cd /opt/payara-full-6.2023.4/glassfish/bin

    ./asadmin 
    ```

    * Izvršite sljedeće naredbe:
    ```bash
    create-jmsdest --desttype queue jms_nwtis_queue

    create-jms-resource --restype jakarta.jms.ConnectionFactory jms/NWTiS_mkovac_qf

    create-jms-resource --restype jakarta.jms.Queue jms/NWTiS_mkovac
    ```

    * Sada možete provjeriti postojeće JMS hostove:

    ```bash
    list-jms-hosts
    ```

    * Ako želite kasnije obrisati resurse, izvršite sljedeće naredbe:

    ```bash
    delete-jms-resource jms/NWTiS_mkovac
    delete-jms-resource jms/NWTiS_mkovac_qf
    delete-jmsdest jms_nwtis_queue
    ```

  * Smjestite se u korijenski direktorij projekta (koristeći File Browser) - potrebno radi pokretanja skripti (više informacija u nastavku)

  Daljnje korake pratite u sekciji <a href="#-upotreba">Upotreba</a>.

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>

## 🚀 Upotreba

⚠️ Za ispravan rad važan je redoslijed pokretanja

### Baza podataka (Docker)

* Smjestiti se u korijenski direktorij projekta (`mkovac-projekt`) i izvršiti naredbu (`Desni klik > Open in Terminal`):

  ```bash
  ./scripts/pokretac.sh
  ```

  <div align="center">
    <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_1.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_1.png"></a>
  </div>

  ⚠️ Provjeriti je li uspješno pokrenuto, jer inače aplikacije koje ovise o bazi podataka neće raditi ispravno, odnosno neće se moći deployati.

* Unutar Eclipse IDE u perspektivi DBeaver sada možete pristupiti bazi i putem `200.20.0.3:9001`

  👉🏽 Potrebno je kreirati nove veze te zamijeniti `localhost` s `200.20.0.3:9001`

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_15.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_15.png"></a>
    </div>

### mkovac_aplikacija_1

* Izvršiti kompiliranje:

  👉🏽 Odabrati jedan od načina:

  * Eclipse IDE:

    ⚠️ Potrebno je uvesti konfiguracije u Eclipse IDE (opisano u sekciji <a href="#pokretanje">Pokretanje</a>)

    `Run Configurations... > mkovac_aplikacija_1 - install`

  * Maven:

    Smjestiti se u direktorij `mkovac_aplikacija_1` i izvršiti naredbu (`Desni klik > Open in Terminal`):

    ```bash
    mvn clean package install
    ```

* Smjestiti se u direktorij `mkovac_aplikacija_1` i izvršiti naredbu (`Desni klik > Open in Terminal`):

  ```bash
  java -jar /home/NWTiS_2/mkovac/mkovac-projekt/mkovac_aplikacija_1/target/mkovac_aplikacija_1-1.0.0.jar NWTiS_mkovac_1.txt
  ```

  ⚠️ Potrebno je prilagoditi putanju do izvršne datoteke

* Aplikacija je spremna za rad. 

  (Opcionalno) Testiranje rada može se obaviti preko klase `TestKlijent.java` (Unutar Eclipse IDE: `Desni klik > Run As > Java Application`). Komande su opisane u sekciji <a href="#-opis-projekta">Opis projekta</a>.

### mkovac_aplikacija_2

* Smjestiti se u direktorij `mkovac_aplikacija_2`

* Pokretanje baze (lokalno):

  ⚠️ Korišteno za inicijalno kreiranje shema i slično. Nije potrebno pokretati osim ako nije potrebno uređivati bazu. Javit će se greška ako je već pokrenuta baza na Dockeru (time je baza dostupna i na `localhost:9001` i na adresi kontejnera `200.20.0.3:9001`). 

  * U direktoriju `mkovac_aplikacija_2` otvorite terminal (`Desni klik > Open in Terminal`) i izvršite naredbu:

  ```bash
  ./scripts/pokreniBazu.sh
  ```

* Pokretanje Payara Web servera (lokalno):

  * U direktoriju `mkovac_aplikacija_2` otvorite terminal (`Desni klik > Open in Terminal`) i izvršite naredbu:

    ```bash
    ./scripts/pokreniServer.sh
    ```

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_2.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_2.png"></a>
    </div>

  * Payara Console dostupna je na `localhost:4848`

    * Klikom na `Applications` moguće je vidjeti popis isporučenih (deployanih) aplikacija

    * Klikom na `server (Admin Server) > View Raw Log` moguće je uživo pratiti logove servera (kontrola rada aplikacija, iznimke i sl.)

* Sada se može obaviti isporuka aplikacije:

  * Unutar Eclipse IDE izvršite konfiguraciju `mkovac_aplikacija_2 - redeploy`

    ⚠️ U slučaju greške kod deploya, provjeriti jesu li ispravno napravljeni svi prethodni koraci (uvjet je ispravno pokrenuta i podešena baza podataka te pokrenuta `mkovac_aplikacija_1`).

  * Provjeriti je li aplikacija isporučena na Payara server (`Payara Console > Applications`)

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_3.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_3.png"></a>
    </div>

  * Aplikacija je dostupna na adresi `localhost:8080/mkovac_aplikacija_2`

    👉🏽 Unutar `Payara Console > Applications` kliknite na `Launch` te na poveznicu aplikacije.

    ⚠️ Na početnoj adresi `localhost:8080/mkovac_aplikacija_2` ne nalazi se ništa! Testiranje se može provesti putem preglednika ili putem Postman-a koristeći putanje definirane u sekciji <a href="#-opis-projekta">Opis projekta</a>.

* Pokretanje Payara Micro servera (Docker):

  * U direktoriju `mkovac_aplikacija_2` otvorite terminal (`Desni klik > Open in Terminal`) i izvršite naredbu:

    ```bash
    ./scripts/pokretac.sh
    ```

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_4.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_4.png"></a>
    </div>

  * Aplikacija je sada dostupna i na `200.20.0.4:8080/mkovac_aplikacija_2`

  <br>

  ⚠️ Nakon svakog ponovnog deploya aplikacije (deploy se vrši na lokalni server), potrebno je ponovno izvršiti ovaj korak kako promjene bile dostupne i unutar Docker slike.

  <br>
  
* Sada se može testirati aplikacija

  👉🏽 Testiranje je moguće napraviti preko preglednika, no preporuka je koristiti pripremljenu skriptu za Postman: <a href="./mkovac_aplikacija_2/mkovac_aplikacija_2.postman_collection.json">`mkovac_aplikacija_2.postman_collection.json`</a>

  * Pokrenite Postman i napravite uvoz skripte:

    * Kliknite na `File > Import...`

    * Uvezite datoteku `mkovac_aplikacija_2.postman_collection.json`

      Datoteka se nalazi na putanji:

      ```bash
      mkovac-projekt/mkovac_aplikacija_2/
      ```

  * Za početak možete provjeriti status poslužitelja mkovac_aplikacija_1 koristeći `nadzor_a`:

    ```bash
    [GET] http://200.20.0.4:8080/mkovac_aplikacija_2/api/nadzor
    ```

  * Potom izvršite inicijalizaciju sustava koristeći `nadzor_b.2` (bitno za nastavak rada):

    ```bash
    [GET] http://200.20.0.4:8080/mkovac_aplikacija_2/api/nadzor/INIT
    ```

  * Sada možete testirati preostale naredbe, primjerice `aerodromi_d`:

    ```bash
    [GET] http://200.20.0.4:8080/mkovac_aplikacija_2/api/aerodromi/LDZA/udaljenosti?odBroja=1&broj=1000
    ```

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_5.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_5.png"></a>
    </div>

    <br>

    ⚠️ Ukoliko niste obavili inicijalizaciju servera `mkovac_aplikacija_1` (ili ste ga pauzirali) javlja se pogreška `403 - Forbidden`:

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_6.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_6.png"></a>
    </div>

* Gašenje Payara Web servera:

  Iz terminala u kojem se pokretali server, izvršite naredbu:

    ```bash
    ./scripts/zaustaviServer.sh
    ```

  ⚠️ Za nastavak rada potrebno je ugasiti server, jer će se nadalje koristiti Payara Full, koji ne može biti pokrenut istovremeno sa Payara Web serverom (koriste se identične postavke). Gašenje lokalnog Payara Web servera neće utjecati na rad Payara Micro servera pokrenutog na Dockeru!


### mkovac_aplikacija_3

  * Ugasiti Payara Web server (ako još nije ugašen)

  * Smjestiti se u direktorij `mkovac_aplikacija_3`

  * Pokretanje Payara Full servera (lokalno)

    ⚠️ Prije pokretanja provjerite jeste li podesili JMS poruke (opisano u sekciji <a href="#priprema">Priprema</a>)

    * U direktoriju `mkovac_aplikacija_3` otvorite terminal (`Desni klik > Open in Terminal`) i izvršite naredbu:

      ```bash
      ./scripts/pokreniServer.sh
      ```

    * Payara Console dostupna je na `localhost:4848`

      👉🏽 Vrijede ista pravila kao i za Payara Full, pa se može samo osvježiti stranica (ako nije ugašena)

      * Klikom na `Applications` moguće je vidjeti popis isporučenih (deployanih) aplikacija

      * Klikom na `server (Admin Server) > View Raw Log` moguće je uživo pratiti logove servera (kontrola rada aplikacija, iznimke i sl.)

  * Sada se može obaviti isporuka aplikacije:

    * Unutar Eclipse IDE izvršite konfiguraciju `mkovac_aplikacija_3 - redeploy`

      NAPOMENE:

      ⚠️ U slučaju greške kod deploya, provjeriti jesu li ispravno napravljeni svi prethodni koraci (uvjet je ispravno pokrenuta i podešena baza podataka, pokrenuta i <strong>inicijalizirana (naredba `INIT`)</strong> `mkovac_aplikacija_1` te ispravno podešene JMS poruke). Također, problem se može javiti s bibliotekom `nwtis_rest_lib v 3.0.0` koja se nalazi na repozitoriju fakulteta te ako isti u budućnosti ne bude dostupan, tada treba podesiti lokalno (biblioteka se nalazi u `nwtis/nwtis_rest_lib`, dok se u `pom.xml` projekta nalazi zakomentirana instrukcija)

      ⚠️ Aplikacija preuzima letove sa servisa Open Sky Network, međutim program ima i alternativno rješenje ako servis padne, te u tom slučaju treba promijeniti postavku u datoteci `nwtis/mkovac-projekt/mkovac_aplikacija_3/src/main/webapp/WEB-INF/NWTiS.db.config_3.xml`:

      ```bash
      <entry key="preuzimanje.klijent">OSKlijent</entry>

      #OSKlijent - default, koristi se Open Sky Network za preuzimanje letova
      #OSKlijentBP - alternativno, koristi se baza podataka s FOI-ja u kojoj se nalaze preuzeti letovi od ranije (čini se da više nije dostupno)
      ```

    * Provjeriti je li aplikacija isporučena na Payara server (`Payara Console > Applications`)

  * Pratite rad aplikacije u `server (Admin Server) > View Raw Log`

    ⚠️ Isporukom aplikacije u prethodnom koraku (ako je sve bilo ispravno) se automatski započinje s radom aplikacije, dakle krenut će se preuzimati letovi sukladno zadanim postavkama.

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_8.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_8.png"></a>
    </div>

  * Preuzete letove možete pregledati u Eclipse IDE koristeći DBeaver

    👉🏽 Možete izvršiti upite iz datoteke `nwtis/mkovac-projekt/Scripts/mkovac_aplikacija_3.sql` (detaljnije pročitajte u <a href="../dokumentacija/projekt/NWTiS_2022_2023_v1.0.pdf"><strong>opisu projekta</strong></a>).

  * Zaustavljanje aplikacije:

    Na `Payara Console > Applications` odaberite iz popisa `mkovac_aplikacija_3-1.0.0` te kliknite na `Undeploy`

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_7.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_7.png"></a>
    </div>

### mkovac_aplikacija_4

* Ako je Payara Full već pokrenut (i sve prethodne aplikacije), sve što je potrebno napraviti jest isporuka aplikacije kroz Eclipse IDE:

  * Unutar Eclipse IDE izvršite konfiguraciju `mkovac_aplikacija_4 - redeploy`

  * Provjeriti je li aplikacija isporučena na Payara server (`Payara Console > Applications`)

  ⚠️ Ako se pojavi greška u kreiranju wsdl-ova kod isporuke (redeploy) unutar Eclipse IDE, potrebno je napraviti sljedeće:

  `Desni klik` na `mkovac_aplikacija_4` i isprobati nešto od sljedećeg:
    1. `Refresh`
    2. `Maven > Update Project`
    3. Ponovno pokrenuti Eclipse IDE

  ⚠️ U slučaju druge greške kod deploya, provjeriti jesu li ispravno napravljeni svi prethodni koraci (uvjet je ispravno pokrenuta i podešena baza podataka te pokrenuta i <strong>inicijalizirana (naredba `INIT`)</strong> `mkovac_aplikacija_1`)

* Sada se može testirati aplikacija:

  * Pokrenite SoapUI i napravite uvoz skripte:

      * Kliknite na gumb `Import`

      * Učitajte datoteku `mkovac-aplikacija-4-soapui-project.xml`

        Datoteka se nalazi na putanji:

        ```bash
        mkovac-projekt/mkovac_aplikacija_4/
        ```

  * Za početak dodajte novog korisnika koristeći zahtjev `dodajKorisnika`

    ⚠️ Ovo je važno jer je za određene zahtjeve potrebna autentikacija

  * Potom možete isprobati ostale zahtjeve

    Ovdje je primjer zahtjeva `dajMeteo` i odgovora

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_9.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_9.png"></a>
    </div>

### mkovac_aplikacija_5

* Ako je Payara Full već pokrenut (i sve prethodne aplikacije), sve što je potrebno napraviti jest isporuka aplikacije kroz Eclipse IDE:

  * Unutar Eclipse IDE izvršite konfiguraciju `mkovac_aplikacija_5 - redeploy`

  * Provjeriti je li aplikacija isporučena na Payara server (`Payara Console > Applications`)

  ⚠️ U slučaju greške kod deploya, provjeriti jesu li ispravno napravljeni svi prethodni koraci (minimalni uvjet je ispravno pokrenuta i podešena baza podataka, pokrenuta i <strong>inicijalizirana (naredba `INIT`)</strong> `mkovac_aplikacija_1` te pokrenuta `mkovac_aplikacija_4` (autentikacija)). Aplikacija `mkovac_aplikacija_3` nije od krucijalnog značaja za rad.

* Pristupite aplikaciji koristeći `Launch` unutar `Payara Console > Applications` ili putem adrese http://localhost:8080/mkovac_aplikacija_5/

  * Za početak se prijavite s korisničkim imenom i lozinkom kreiranom u `mkovac_aplikacija_4` ili obavite registraciju ako niste

  * Upravljanje poslužiteljem AP1 (`mkovac_aplikacija_1`):

    <img src="../dokumentacija/projekt/AP5_1.gif">

  * Pregled aerodroma:

    <img src="../dokumentacija/projekt/AP5_2.gif">

  * Izračun udaljenosti između dva aerodroma:

    <img src="../dokumentacija/projekt/AP5_3.gif">

  * Pregled letova s aerodroma u zadanom intervalu:

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_12.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_12.png"></a>
    </div>

  * Pregled dnevnika (log):

    <div align="center">
      <a href="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_11.png"><img alt="nwtis_demo" src="https://raw.githubusercontent.com/mkovac700/nwtis/docs/dokumentacija/projekt/Screenshot_11.png"></a>
    </div>

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>

## ⛏️ Korišteni alati

<div align="center">
  
  <a href="https://www.java.com/en/"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original-wordmark.svg" width=100/></a>
  <a href="https://www.eclipse.org/"> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/eclipse/eclipse-original.svg" width=100/></a>
  <a href="https://maven.apache.org/"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/maven/maven-original.svg" width=100/></a>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jquery/jquery-plain-wordmark.svg" width=100/>
  <a href="https://www.docker.com/"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg" width=100/></a>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/bash/bash-original.svg" width=100/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg" width=100/>
          
          
</div>

<p align="right">(<a href="#readme-top">povratak na vrh</a>)</p>


