# BeautyLine

### Introduzione
Il seguente progetto presenta lo sviluppo di un e-commerce pensato per la corretta gestione della vendita dei prodotti di un centro estetico e della prenotazione di appuntamenti.
Nel dettaglio, verte sulla possibilità di visionare prodotti e trattamenti per poi proseguire con l'eventuale acquisto di prodotti o con la prenotazione di trattamenti. Al fine di comprenderne meglio l'utilità, si riportano di seguito, alcune delle sue funzionalità.
Senza previa registrazione dell'utente, consente l'individuazione dei prodotti, dei trattamenti, degli eventi e dei pacchetti di interesse.
Previa registrazione dell'utente, consente di prenotare i trattamenti specificando l'orario e il giorno e l'orario dell'appuntamento oppure di acquistare i prodotti effettuando il pagamento per mezzo del sistema di pagamenti digitali Stripe. Inoltre, sarà possibile visualizzare gli eventi e acquistare pacchetti di prodotti, a tali prodotti sarà poi allegato un codice, gli utenti possono scambiarsi i pacchetti tramite l'utilizzo di questo codice nella pagina dedicata. Infine, gli utenti possono commentare un determinato prodotto o trattamento e questo sarà visibile agli altri utenti.
Il corretto utilizzo di tale e-commerce favorisce, inoltre, una più semplice ed efficace amministrazione degli appuntamenti, della vendita di prodotti e della comunicazione relativa a eventi e pacchetti da parte dei proprietari dell'attività commerciale.
In conclusione, l'applicazione in questione ha il doppio obiettivo di facilitare il cliente nella ricerca di un adeguato prodotto o trattamento, ottimizzando i tempi, nonchè, grazie al sostegno della digitalizzazione, si presenta come un valido aiuto per i gestori dell'attività.

### Analisi dei requisiti e funzionalità
Le sue funzionalità si suddividono in tre categorie:
 - _**Pubbliche**_
 - _**Utente**_
 - _**Amministratore**_
 
 Le funzionalità _**pubbliche**_ sono:
  - la login sia di un utente che di un'amministratore, necessaria per effettuare l'accesso al proprio accont e alla propria area riservata (dati personali)
  - la sign up per fornire l'interfaccia con utenti non ancora registrati/autenticati mediante la compilazione dei propri dati (nome, cognome, email, password e cellulare)
  - la Forgot Password, che in caso di dimenticanza della stessa, l'utente ne riceve in modo immediato una nuova
  
 Le funzionalità _**utente**_ sono:
  - Cambio password
  - Cambio dati personali
  - Visualizzazione di:
    - Profilo utente
    - Home page
    - Trattamenti
    - Commenti relativi ai trattamenti
    - Prodotti
    - Commenti relativi ai prodotti
    - Ordini effettuati
    - Prenotazioni di trattamenti effettuate
    - Pacchetti acquistati e/o ricevuti
    - Eventi & Offerte
  - Pubblicazione di commenti
  - Prenotazione dei trattamenti
  - Acquisto dei prodotti
  - Acquisto dei pacchetti
  - Scambio di pacchetti
  
   Le funzionalità _**amministratore**_ sono:
  - Inserimento/aggiornamento/rimozione di trattamenti
  - Inserimento/aggiornamento/rimozione di prodotti
  - Inserimento/aggiornamento/rimozione di eventi
  - Inserimento/aggiornamento/rimozione di pacchetti
  - Inserimento/aggiornamento/rimozione di commenti
  - Inserimento/aggiornamento/rimozione di scambi di pacchetti
  - Aggiornamento/rimozione di utenti
  - Aggiornamento/rimozione di prenotazioni
  - Eliminazione di ordini
  - Visualizzazione di:
    - Home page
    - Trattamenti
    - Commenti relativi ai trattamenti
    - Prodotti
    - Commenti relativi ai prodotti
    - Eventi & Offerte
    
### API e Documentazione Swagger

Sono state sviluppate svariate API, affinché tutte le funzionalità vengano implementate.
É possibile visualizzare la loro documentazione al seguente link: [Documentazione Swagger BeautyLine (https://app.swaggerhub.com/apis/AlfonsoCiampa/BeautyLine/1.0.0/)

### Schema ER DataBase
Il servizio di database scelto per BeautyLine è MySql, il quale permette la gestione di un database relazionale.
Quello in questione è composto dalle seguenti tabelle:

 - Account: tutte le informazioni riguardanti ogni singolo account, tra cui credenziali, ruolo (User/Admin)
 - Consumer: tutte le informazioni riguardanti ogni singolo utente, tra cui anagrafica e numero di cellulare
 - Booking: tutte le informazioni sulle prenotazioni, raccogliendo chi, quando e cosa ha prenotato
 - Event: tutte le informazioni sui vari eventi, indicando data di inizio, data di fine e descrizione
 - Gift: tutte le informazioni sugli scambi di pacchetti, indicando a chi e cosa è stato regalato
 - Order: tutte le informazioni sugli ordini, specificando chi ha eseguito l'ordine, il prezzo totale e il codice di un eventuale pacchetto acquistato
 - OrderItem: tutte le informazioni sui vari elementi di un ordine, indicando il prezzo di ogni prodotto e il suo ID
 - Package: tutte le informazioni sui vari pacchetti, tra cui descrizione, prezzo e codice
 - PackageItem: tutte le informazioni sui vari items per ogni package
 - Product: tutte le informazioni sui prodotti, raccogliendo descrizione, nome, prezzo e quantità
 - ServiceComment: tutte le informazioni riguardanti ogni singolo commento, tra cui chi e quando l'ha pubblicato, il contenuto del commento e il tipo di servizio a cui si riferisce
 - Treatment: tutte le informazioni sui vari trattamenti, raccogliendo descrizione, nome, prezzo e durata.

------->>>>>>> ER Database Diagram <<<<<<<-------

### Use Case Diagram
Sulla base delle funzionalità descritte precedentemente sono stati creati degli Use Case Diagram disponibili in ./UseCaseDiagram/ nei formati .puml e .png, i quali descrivono le funzioni o servizi offerti dal sistema, così come sono percepiti e utilizzati dagli attori che interagiscono col sistema stesso nei seguenti 3 casi d’uso:

- Funzionalità pubbliche
- Funzionalità utente
- Funzionalità amministratore

### Funzionalità pubbliche
------->>>>>>> Use Case Diagram <<<<<<<-------

### Funzionalità utente
------->>>>>>> Use Case Diagram <<<<<<<-------

### Funzionalità amministratore

------->>>>>>> Use Case Diagram <<<<<<<-------

### Sequence Diagram
Sono stati creati dei Sequence Diagram disponibili in ./SequenceDiagram/ nei formati .puml e .png. Questi, descrivono la successione di operazioni e messaggi di risposta tra i vari componenti dell’intero sistema, nei seguenti 3 casi d’uso:

 - Login
 - Aggiunta veicolo
 - Pagamento multa

#### Login
------->>>>>>> Sequence Diagram <<<<<<<-------

#### Prenotazione
------->>>>>>> Sequence Diagram <<<<<<<-------

#### Pagamento ordine
------->>>>>>> Sequence Diagram <<<<<<<-------


### Struttura del codice
- Linguaggio di programmazione: Java
- Framework: Spring Boot
- Realizzazione dei package seguendo il modello:
    - configuration (configurazioni d'interfaccia con DataBase e MailTrap)
    - controller (gestione delle richieste, chiamando i metodi dai vari services)
    - entity (vero e proprio mapping delle tabelle del DataBase)
    - repository (gestione delle query)
    - security (gestione dell'autenticazione)
    - service (insieme di metodi pronti a svolgere operazioni, tra cui Stripe)

### Piano di Testing

I tests implementati si articolano in:

* Test per i Services: per controllare le operazioni svolte dai vari metodi
    * Test per le operazioni pubbliche
    * Test per le operazioni dell'utente
    * Test per le operazioni dell'amministratore
* Test per i Controllers: per verificare gli end points e lo status code restituito
    * Test per il Controller pubblico
    * Test per il Controller utente
    * Test per il Controller amministratore
    
#### Rapporto sull'andamento dei test

I log del successo dei test sono visualizzabili al seguente link: ------->>>>>>> TestResults LINK <<<<<<<-------

### Ipotetici miglioramenti futuri

Il sistema server side attualmente non predispone di:
- un servizio di chat tra clienti e assistenza
- una gestione dell'oscurazione dei prodotti (nel caso di una momentanea indisponibilità del prodotto)
- una gesione dei limiti alle prenotazioni (attualmente il limite è di due, ma in futuro sarà modificabile tramite API)

Le funzioni sopra elencate fungono da possibili migliorie realizzabili per il perfezionamento dell’esperienza utente.
