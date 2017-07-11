# Progetto-L-Android

NOTA:    
In entrambe le app è stato inserito un easter egg per permettere la configurazione dinamica dell'IP e della PORTA utilizzati nella comunicazione HTTP dell'APP.

Utilizzo:    
Nella schermata di login, scrivere nel campo "username" la stringa "config", lasciare il campo password VUOTO.
quando viene premuto il tasto LOGIN, apparira' una textView che permetterà di modificare i campi prima menzionati inserendo una stringa nella forma "ip:port", dove IP rappresenta l'indirizzo IP dell'host sul quale viene lanciato il WebServer (N.B. non il sistema centrale) e PORT la porta sul quale viene lanciato il server TomCat (di default 8080). Con il tasto OK i dati vengono aggiornati e può essere utilizzata correttamente l'app.

PROFILI DI PROVA:
Per il test dell'app sono stati creati diversi profili:   

USER -> username = ADMIN , password = ADMIN.

COLLECTOR -> username = COLLECTOR , password = COLLECTOR.

Per quanto riguarda l'user è possibile registrarsi, verificando così anche l'invio delle e-mail di conferma e di acquisto.
