
/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Set;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main
{
    
    final static String DATE_FORMAT = "dd-MM-yyyy";
        
    public static void main(){
        String ficheiro = "save";
        // carrega ficheiro
        DataBase db = new DataBase();
        
        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("save"));
            db = (DataBase) oin.readObject();
            oin.close();   
            System.out.println("Ficheiros carregados com sucesso");
        }
        catch(IOException e)
            { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e)
            { System.out.println(e.getMessage()); }
        
        
        //printa o primeiro menu
        int opcao = 1;
        while( opcao != 0 ){
            System.out.println("\n *** UMER ***");
            String[] opcoes = {"Login utilizadores",
                               "Login Empresa",
                               "Sign In",
                               "Top 10 de Clientes",
                               "5 piores motoristas"
                               /*"APAGAR_Imprimir",
                               "APAGAR_Popular"
                               */};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
            
            switch( opcao ){
                case 1: login(db);
                        break;
                case 2: loginEmpresa(db);
                        break;
                case 3: signIn(db);
                        break;
                case 4: System.out.println("-------TOP10-------");
                        for( Client c : db.top10())
                            System.out.println(c.getEmail());
                        System.out.println("\n-------------------");  
                        break;
                case 5: System.out.println("-------5Piores-------");
                        for( User c : db.PioresCondutores())
                            System.out.println(c.getEmail());
                        System.out.println("\n-------------------"); 
                        break;
                case 6: System.out.println(db.imprimeTUDO());
                        break;
                case 7: db.PopularDataBase();
                        System.out.println("********Done********");
                        break;
            }
        }
        //salva ficheiro
        try{
            db.gravaEmObjStream(ficheiro);
        }
        catch( IOException e)
            { System.out.println(e.getMessage()); }
        
        System.out.println("Obrigado pela seu preferencia, ate sempre!");
    
    }
    
    private static void login(DataBase db){       
        Scanner input = new Scanner(System.in);   
        System.out.print("\n *** LogIn ***\nMail: ");
        String mail = input.nextLine(); 
        System.out.print("Palavra passe: ");
        String pass = input.nextLine(); 
        
        boolean valido = db.existeUtilizador(mail) && db.getUser(mail).validaLogin(pass);
       
        if(valido){
            //redireciona para o menu correto
            User aux = db.getUser(mail);
            
            if( aux instanceof Driver)
                menuDriver( (Driver) aux, db);
            if( aux instanceof Client )
                menuClient( (Client) aux, db);
        }else
            System.out.println("O utlizador e a password não correspondem");
    }
                 
    private static void menuDriver( Driver d, DataBase db){
        int opcao = -1;
        
        while( opcao != 0){
            System.out.println("\n *** Menu ***");
            
            String[] opcoes = {"A trabalhar-" + d.estaDisponivel(),
                               "Fazer viagem em fila de espera - "+ d.numViagensEmEspera(db) +" viagens",
                               "Area pessoal",
                               "Ver carro privado"};
            Menu menu = new Menu(opcoes);
            
            if( d instanceof TaxiDriver){
                TaxiDriver td = (TaxiDriver) d;
                String[] opcoes2= {"A trabalhar-" + td.estaDisponivel(),
                                   "Fazer viagem em fila de espera - "+ d.numViagensEmEspera(db) +" viagens",
                                   "Area pessoal",
                                   "Ver carro privado",
                                   "Ver carro de empresa",
                                   "Na empresa - " + td.getDispEmpresa()};
                menu = new Menu(opcoes2);
            }
            
            menu.executa();
            opcao = menu.getOpcao();
    
            switch( opcao ){
                case 1: boolean x = ! d.estaDisponivel();
                        d.setDisponivel(x);
                        if( !x && d instanceof TaxiDriver){
                            TaxiDriver td = (TaxiDriver) d;
                            td.setDispEmpresa(false,db);
                        }
                        break;
                case 2: try{
                            d.fazViagemEmEspera(db);
                        }
                        catch( ViagemNaoExistenteException e){ System.out.println("Não tem viagens em fila de espera");}
                        break;
                case 3: areaPessoal(d,db);
                        break;
                case 4: verCarro( d.getCarroPessoal(), db);
                        break;
                case 5: TaxiDriver td = (TaxiDriver) d;
                        if( td.getDispEmpresa())
                            System.out.println("Carro " + td.getCarroDeEmpresa());
                        else
                            System.out.println("Não tem carro atribuido");
                        break;
                case 6: td = (TaxiDriver) d;
                        td.setDispEmpresa( ! td.getDispEmpresa(),db);
                        break;
            }
                
        }
    }
    
    private static void menuClient( Client c, DataBase db){
        int opcao = -1;
        
        while( opcao != 0){
            System.out.println("\n *** Menu ***");
            String[] opcoes = {"Fazer uma viagem",
                               "Area pessoal"};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
                
            switch( opcao ){
                case 1: fazerViagem(c,db);
                        break;
                case 2: areaPessoal(c,db);
                        break;
            }
                
        }
    }
    private static void areaPessoal( Driver d, DataBase db){
        int opcao = -1;
        
        while( opcao != 0){
            System.out.println("\n *** Area Pessoal ***");
            String[] opcoes = {"Listar viagens",
                               "Listar viagens entre datas",
                               "Dados pessoais"};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
                
            switch( opcao ){
                case 1: listaViagens(d,db);
                        break;
                case 2: listaViagensEntreDatas(d,db);
                        break;
                case 3: dadosPessoais(d);
                        break;
            }
                
        }
    }
    private static void areaPessoal( Client c, DataBase db){
        int opcao = -1;
        
        while( opcao != 0){
            System.out.println("\n *** Area Pessoal ***");
           
            String[] opcoes = {"Listar viagens",
                               "Listar viagens entre datas",
                               "Dar nota a uma viagem",
                               "Dados pessoais"};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
            
            switch( opcao ){
                case 1: listaViagens(c,db);
                        break;
                case 2: listaViagensEntreDatas(c,db);
                        break;
                case 3: darNota(c,db);
                        break;
                case 4: dadosPessoais(c);
                        break;
            }
                
        }
    }
    
    private static void listaViagens( ImplementaViagens obj, DataBase db){
         if( obj.getViagens().size() == 0)
            System.out.println( "Nenhuma viagem efetuada" );
         else{
            int opcao = -1;
            while( opcao != 0){
                 List<String> viagens = obj.listaViagens(db);
                 int numViagens = viagens.size();
                 String[] arrayViagens = new String[ numViagens];
                 for( int i = 0 ; i < numViagens; i++)
                    arrayViagens[i] = viagens.get(i);
                    
        
                 System.out.println("\n ***Viagens ***");
                 System.out.println("Escolha a viagem 1-"+numViagens+" para ver mais informação");
                 Menu menu = new Menu(arrayViagens);
                 menu.executa();
                 opcao = menu.getOpcao();
                
                 if( opcao <= numViagens && opcao>0){
                    Travel v = db.getViagem(obj.getViagem(opcao-1) );
                    System.out.println("\n---------------\n" + v.toString() +"\n---------------\n");
                 } 
            }
            
        }
    }
    private static void listaViagensEntreDatas(ImplementaViagens obj, DataBase db){
        if( obj.getViagens().size() == 0)
            System.out.println( "Nenhuma viagem efetuada" );
        else{
            int opcao = -1;
            while( opcao != 0){
                // le as datas de pequisa
                boolean flag = false;
                LocalDate inicio = LocalDate.of(1970,1,1);
                LocalDate fim = LocalDate.of(1970,1,1);
                do{
                    flag = false;
                    System.out.println("Intoduza as datas (no formato dd-MM-yyyy)");
                   
                    try{
                         System.out.print("Inicio: ");
                         inicio = leData();
                         System.out.print("Fim: ");
                         fim = leData();
                    }
                    catch(  DateTimeParseException e){
                        System.out.println("As datas não são válidas");
                        flag = true;
                    }
                    flag = flag || inicio.isAfter(fim);
                }while(flag);
                
                List<String> viagens = obj.listaViagens(db,inicio,fim);
                int numViagens = viagens.size();
                if( numViagens == 0){
                    System.out.println( "Nenhuma viagem efetuada" );
                    return ;
                }
                String[] arrayViagens = new String[ numViagens];
                for( int i = 0 ; i < numViagens; i++)
                    arrayViagens[i] = viagens.get(i);
                    
        
                System.out.println("\n ***Viagens ***");
                System.out.println("Escolha a viagem para ver mais informação");
                Menu menu = new Menu(arrayViagens);
                menu.executa();
                opcao = menu.getOpcao();
                
                if( opcao <= numViagens && opcao>0){
                    Travel v = db.getViagem(obj.getViagem(opcao-1) );
                    System.out.println("\n---------------\n" + v.toString() +"\n---------------\n");
                }
            }
            
        }
    }
    private static void darNota(Client c,DataBase db){
        int nota,num;
        nota = num = -1;
        if ( c.getViagens().size() == 0){
            System.out.println("Nenhuma viagem efetuada");
            return ;
        }
        do{
            System.out.println("Numero da viagem (1-"+ c.getViagens().size() + "):" );
            num = leInteiro( c.getViagens().size() );
        }while( num == -1);
        do{
            System.out.println("Nota de 1-100: ");
            nota = leInteiro(100);
        }while( nota == -1);
        
        if( db.getViagem(num-1).getNota() != -1)
            System.out.println("Já atribui nota a esta viagem");
        else{
            Travel t = db.getViagem(num-1);
            t.setNota( nota);
            System.out.println("Nota abribuida com sucesso");
            //atualiza classificacao media do driver
            Driver d = (Driver) db.getUser(t.getCondutor());
            double aux = d.getClassificaçao();
            double size = (double) d.getViagens().size();
            d.setClassificaçao((aux *size + nota)/(size +1));
        }
    
    }
    
    private static void dadosPessoais(User u){
        Scanner input = new Scanner(System.in);
        int opcao = -1;
        
        while(opcao != 0){
            System.out.println("Escolha (1-5) para alterar os valores");
            String[] opcoes = {"Nome: " + u.getNome(),
                               "Email: " + u.getEmail(),
                               "Morada: " + u.getMorada(),
                               "Data de nascimento: " + u.getDataDeNascimento(),
                               "Palavra Passe: *****"};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
            
            switch( opcao ){
                case 1: System.out.println("Insira o nome");
                        u.setNome( input.nextLine());
                        break;
                case 2: System.out.println("Não é possivel alterar o email\n");
                        break;
                case 3: System.out.println("Insira a morada");
                        u.setMorada( input.nextLine());
                        break;
                case 4: LocalDate data = LocalDate.now();
                        boolean flag =false;
                        do{
                            if (flag) System.out.println("Insira uma data correta");
                            flag = false;
                            System.out.println("Dia de nascimento ( no formato " + DATE_FORMAT + "):");
                            try{
                                data = leData();
                            }
                            catch( DateTimeParseException e){ flag = true; }
                        }while( flag && isLegalDate(data));
                        u.setDataDeNascimento(data);
                        break;
                case 5: System.out.println("Insira a palvra passa antiga:");
                        String antiga = input.nextLine();
                        System.out.println("Insira a nova palvra passa:");
                        String nova = input.nextLine();
                        try{
                            u.setPassWord( nova, antiga);
                            System.out.println("Palavra passe alterada com sucesso\n");
                        }
                        catch ( PalavraPasseErradaException e){
                            System.out.println("Palavra passe errada\n");
                        }
                        break;
            }
        }
    }
    
    private static void verCarro(String  matricula, DataBase db){
        Veiculo v = db.getVeiculo( matricula);
        
        System.out.println("---------------\n" + v.toString() + "\n---------------\n");
        String[] opcoes = {"Alterar preço por kilometro","Alterar velocidade media","Ver viagens"};
        Menu menu = new Menu(opcoes);
        int opcao = -1;
        
        while ( opcao != 0){
            menu.executa();
            opcao = menu.getOpcao();
            
            switch( opcao){
                case 1: boolean flag = false;
                        double preco = -1;
                        do{
                            if(flag) System.out.println("Opção Inválida!!!");
                            System.out.print("Novo preço por kilometro: ");
                            preco = leDouble(100);
                            flag = true;
                        }while(preco == -1);
                        v.setPrecoKm(preco);
                        break;
                case 2: flag = false;
                        double veloc = -1;
                        do{
                            if(flag) System.out.println("Opção Inválida!!!");
                            System.out.print("Velocidade media: ");
                            veloc = leDouble(120);
                            flag = true;
                        }while(veloc == -1);
                        v.setVelocidade(veloc);
                        break;
                case 3: listaViagens( v, db);
                        break;
            }
        }
    }
    
    private static void fazerViagem( Client c,DataBase db){
        
        System.out.println(" *** Fazer Viagem ***\nInsira as suas coordenadas");
        //le coordenadas iniciais
        c.setPosicao( lePosicao() );
        System.out.println("\n");
        
        int opcao = -2;
        boolean flagC, flagV;
        flagC = flagV = false;
        Veiculo v = new Ligeiro();
        Driver d = new Driver();
        String[] opcoes = { "Escolher o veiculo", 
                            "Escolher condutor", 
                            "Veiculo mais proximo",
                            "Ficar em lista de espera"};
        Menu menu = new Menu(opcoes);
        
        while( !flagV && !flagC && (opcao != 3)){
            menu.executa();
            opcao = menu.getOpcao();
                    
            switch( opcao ){
                case 0: return;
                    
                case 1: try{
                            v = escolheVeiculo(db);
                            flagV = true;
                        }
                        catch(CarroNaoExistenteException e){
                            System.out.println("Carro não encontrado");
                        }
                        break;
                case 2: try{
                            d = escolheCondutor(db);
                            flagC = true;
                        }
                        catch(UserNaoExistenteException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                case 3: break;
                case 4: ficarEmListaDeEspera(c,db);
                        return;
                        
            }
        }
        
        System.out.println("Para onde deseja ir?");
        Ponto2D x = lePosicao();
        
        if( flagV ){
            try{
                Travel.fazViagem(c,v,x,db);
                System.out.println("Viagem efectuada com suceeso\n");
            }
            catch( CarroNaoExistenteException e){
                System.out.println("Carro nao existente, algo correu mal");
            }
        }
        else{
            if( flagC){
                Travel.fazViagem(c,d,x,db);
                System.out.println("Viagem efectuada com suceeso\n");
            }else{
                try{
                    Travel.fazViagem(c,x,db);
                    System.out.println("Viagem efectuada com suceeso\n");
                }
                catch( CondutorMaisProximoException e){
                    System.out.println( "Nao existe veiculos disponiveis, tente mais tarde");
                }
            }
       }
        
       // System.out.println("A sua viagem foi realizada com sucesso");
    }
    private static void ficarEmListaDeEspera( Client c,DataBase db){
        String[] opcoes ={ "Escolher o veiculo", "Escolher condutor"};
        Menu menu = new Menu(opcoes);
        int opcao = -1;
        TemFilaDeEspera aux = new Driver();
        boolean flagV, flagC;
        flagC = flagV = false;
        while( !flagV && !flagC){
            menu.executa(); opcao = menu.getOpcao();
            switch( opcao ){
                case 0: return;
    
                case 1: try{
                            aux = (TemFilaDeEspera) escolheVeiculoParaFilaDeEspera(db);
                            flagV = true;
                        }
                        catch(CarroNaoExistenteException e){
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;
                case 2: try{
                            aux = (TemFilaDeEspera) escolheCondutorParaFilaDeEspera(db);
                            flagC = true;
                        }
                        catch(UserNaoExistenteException e){
                                System.out.println(e.getMessage());
                                return;
                        }
            }
            Ponto2D posicao = lePosicao();
                
            Travel.addParaAFila(c,aux,posicao,db);
        }
    }
    private static  Veiculo escolheVeiculoParaFilaDeEspera(DataBase db)throws CarroNaoExistenteException{
        Scanner input = new Scanner(System.in);
        System.out.println("Insira a matricula:");
        String matricula = input.next();
        
        if( db.existeVeiculo(matricula))
            return db.getVeiculo(matricula);
        else
            throw new CarroNaoExistenteException("Veiculo não existente");
    }
    private static Driver escolheCondutorParaFilaDeEspera(DataBase db) throws UserNaoExistenteException{
        Scanner input = new Scanner(System.in);
        System.out.println("Insira o mail:");
        String mail = input.next();
        
        if( db.existeUtilizador(mail))
            return (Driver) db.getUser(mail);
        else
            throw new UserNaoExistenteException("Condutor não existente");
    }
    private static Veiculo escolheVeiculo(DataBase db) throws CarroNaoExistenteException {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira a matricula:");
        String matricula = input.next();
       
        if( db.existeVeiculo( matricula) ){
            if( db.alguemConduz( matricula))
                return db.getVeiculo(matricula);
            else
                throw new CarroNaoExistenteException("Veiculo de momento não está disponivel");
        }
        else
            throw new CarroNaoExistenteException("Veiculo não existente");
    }
        
    private static Driver escolheCondutor(DataBase db) throws UserNaoExistenteException{
        Scanner input = new Scanner(System.in);
        System.out.println("Insira a mail do condutor:");
        String mail = input.next();
        Driver  d;
        if( db.existeUtilizador( mail) ){
            d = (Driver) db.getUser( mail);
            if( d.getDisponivel())
                return d;
            else
                throw new UserNaoExistenteException("Condutor de momento não está disponivel");
        }
        else
            throw new UserNaoExistenteException("Condutor não existe");
    }

    
              //SINGIN//
    //---------------------------//
    
    private static void signIn(DataBase db){
        Scanner input = new Scanner(System.in);
        
        System.out.println("\n *** Sing In ***");
        String[] opcoes = {"Cliente",
                           "Condutor",
                           "Empresa"};
        Menu menu = new Menu(opcoes);
        menu.executa();
        int opcao = menu.getOpcao();
        
        if ( opcao == 0)
            return ;
        if( opcao == 3){
            criaEmpresa(db);
            System.out.println("A sua conta foi registada");
            return;
        }
         
        String email;
        boolean valido;
        do{
            valido = true;
            System.out.println("Email:");
            email = input.nextLine();
            if ( db.existeUtilizador( email)){
                System.out.println("Este email ja existe");
                valido = false;
            }
            if( !email.contains("@")){
                System.out.println("Este email não é válido");
                valido = false;
            }
        }while( !valido );
    
        System.out.println("Nome:");
        String nome = input.nextLine();
        
        System.out.println("Morada:");
        String morada = input.nextLine();
         
        LocalDate data = LocalDate.now();
        boolean flag =false;
        do{
            if (flag) System.out.println("Insira uma data correta");
            flag = false;
            System.out.println("Dia de nascimento ( no formato " + DATE_FORMAT + "):");
            try{
                data = leData();
            }
            catch( DateTimeParseException e){ flag = true; }
        }while( flag && isLegalDate(data));
        
        System.out.println("Palavra passe:");
        String pass = input.nextLine();
        
        User novo = new Client();
        
        switch( opcao ){
                case 1: novo = new Client(email,nome,pass,morada,data, new Ponto2D());
                        break;
                case 2: Veiculo v = criarVeiculo(db);
                        db.add(v);
                        novo = new Driver(email,nome,pass,morada,data,false,v.getMatricula());
                        break;
        }
        
        db.add( novo);
        
        System.out.println("A sua conta foi registada");
        
    }
    private static Veiculo criarVeiculo( DataBase db){
        Scanner input = new Scanner(System.in);
        String[] opcoes = {"Veiculo ligeiro", "Carrinha", "Mota"};
        Menu menu = new Menu(opcoes);
        menu.executa();
        int opcao = menu.getOpcao();
        
        String matricula;
        boolean valida;
        do{
            valida = true;
            System.out.println("Matricula: (AA-22-22)");
            matricula = input.nextLine();
            if ( db.existeVeiculo( matricula)){
                System.out.println("Esta matricula ja exista registada");
                valida = false;
            }
            if( ! validaMatricula( matricula)){
                System.out.println("O formato não é válido");
                valida = false;
            }
        }while( ! valida );
        
        

        double preco= -2;
        do{
            if( preco ==-1) System.out.println("Opção Inválida!!!");
            System.out.println("Preço por km:");
            preco = leInteiro(100);
        }while( preco == -1);
        
        double velocidade= -2;
        do{
            if( preco ==-1) System.out.println("Opção Inválida!!!");
            System.out.println("Velocida media:");
            preco = leInteiro(100);
        }while( preco == -1);
        
        Veiculo novo = new Ligeiro();
        switch( opcao ){
                case 1: novo = new Ligeiro(matricula,preco,velocidade,new Ponto2D());
                        break;
                case 2: novo = new Carrinha(matricula,preco,velocidade,new Ponto2D());
                        break;
                case 3: novo = new Mota(matricula,preco,velocidade,new Ponto2D());
                        break;
        }
        
        return novo;
        
    }
    
    
    //---------------------------------------------------
    
    private static boolean isLegalDate(LocalDate ld) {
        LocalDate hoje = LocalDate.now();
        
        int dia = hoje.getDayOfMonth();
        int mes = hoje.getMonthValue();
        int ano = hoje.getYear();
    
        LocalDate minimo = LocalDate.of(ano-18,mes,dia);
    
        return minimo.isAfter(ld) || minimo.equals(ld);
    }
    private static int countLines(String str){
       String[] lines = str.split("\r\n|\r|\n");
       return  lines.length;
    }
    private static boolean validaMatricula(String matricula){
        Scanner input = new Scanner(System.in);   
        Pattern pattern1 = Pattern.compile("[0-9][0-9]-[0-9][0-9]-[A-Z][A-Z]");
        Pattern pattern2 = Pattern.compile("[0-9][0-9]-[A-Z][A-Z]-[0-9][0-9]");
        Pattern pattern3 = Pattern.compile("[A-Z][A-Z]-[0-9][0-9]-[0-9][0-9]");
        
        Matcher matcher1 = pattern1.matcher(matricula);
        Matcher matcher2 = pattern2.matcher(matricula);
        Matcher matcher3 = pattern3.matcher(matricula);
       
        return matcher1.find() || matcher2.find() || matcher3.find();
    }
        private static Ponto2D lePosicao(){
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        int x,y;
        x = y = 0;
 
        while(flag){
  
            try{
                System.out.print("x = ");
                x = input.nextInt();
                System.out.print("y = ");
                y = input.nextInt();
                flag = false;
            }
            catch(InputMismatchException e){ // se nao for um int
                System.out.println("Insira coordenadas validas");
                input.next();
            }
        }
        
        return new Ponto2D(x,y);
    }
    private static int leInteiro(int x){
        Scanner input = new Scanner(System.in);
        int valor;
        try{
            valor = input.nextInt();
        }
        catch(InputMismatchException e){ // se nao for um int
            valor = -1;
        }
        if( valor <= 0 || valor > x)
            valor = -1;
         
        return valor;
    } 
    private static double leDouble(double x){
        Scanner input = new Scanner(System.in);
        double valor;
        try{
            valor = input.nextDouble();
        }
        catch(InputMismatchException e){ // se nao for um int
            valor = -1;
        }
        if( valor <= 0 || valor > x)
            valor = -1;
         
        return valor;
    }
    private static LocalDate leData()throws DateTimeParseException{
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Scanner input = new Scanner(System.in);   
    
        String data = input.nextLine();
        return LocalDate.parse(data, dtf);
    }

    //----------------------------------------------
    
    private static void criaEmpresa( DataBase db){
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        String nome = "";
        while( flag){
            flag = false;
            System.out.println("Nome da empresa:");
            nome = input.nextLine();
            if (db.existeEmpresa( nome)){
                System.out.println("Nome ja registado");
                flag = true;
            }
        }
        System.out.println("Palavra passe:");
        String pass = input.nextLine();
        
        Company empresa = new Company(nome,pass);
        db.add(empresa);
    }

    private static void loginEmpresa(DataBase db){
        Scanner input = new Scanner(System.in);   
        
        System.out.println(" *** LogIn ***\nNome da empresa:");
        String nome = input.nextLine(); 
        System.out.println("Palavra passe: ");
        String pass = input.nextLine(); 
        
        boolean valido = db.existeEmpresa(nome) && db.getEmpresa(nome).validaLogin(pass);
       
        if(valido){
            //redireciona para o menu correto
            Company empresa = db.getEmpresa(nome);
            
            menuCompany( empresa, db);
        }else
            System.out.println("Nome da empresa e a password não correspondem");
    }

    private static void menuCompany( Company empresa, DataBase db){
        int opcao = -1;
         
        while( opcao != 0){
            System.out.println("\n *** Menu ***");
            String[] opcoes = {"Adicionar condutor",
                               "Despedir condutor",
                               "Criar veiculo",
                               "Listar carros",
                               "Listar motoristas",
                               "Estatisticas"};
            Menu menu = new Menu(opcoes);
            menu.executa();
            opcao = menu.getOpcao();
     
            switch( opcao ){
                case 1: adicionarCondutor(empresa,db);
                        break;
                case 2: despedirCondutor(empresa,db);
                        break;
                case 3: Veiculo x = criarVeiculo(db);
                        empresa.addTaxi( x.getMatricula() );
                        db.add(x);
                        break;
                case 4: imprimeCarros( empresa, db);
                        break;
                case 5: imprimeTaxista(empresa, db);
                        break;
                case 6: estatisticas(empresa, db);
                        break;
                case 7: break;
            } 
        }
    }
    
    private static void imprimeCarros( Company empresa, DataBase db){
        Set<String> carros = empresa.getVeiculos();
        if( carros.size() == 0){
          System.out.println("Não tem nenhum veiculo na sua empresa");
          return ;
        }
        String[] arrayCarros = new String[carros.size()];
        Iterator<String> it = carros.iterator();
        int i = 0;
        while( it.hasNext() )
            arrayCarros[i++] = it.next();
        Menu menu = new Menu(arrayCarros);
    
        while( true){
            menu.executa();
            int opcao = menu.getOpcao();
            if( opcao == 0)
                break;
            verCarro( arrayCarros[i -1],db);
        }
    }
    private static void imprimeTaxista(Company empresa, DataBase db){
       Set<String> condutores = empresa.getCondutores();
       if( condutores.size() == 0){
          System.out.println("Não tem nenhum taxista na sua empresa");
          return ;
        }
       String[] arrayCondutores = new String[condutores.size()];
       Iterator<String> it = condutores.iterator();
       int i = 0;
       while( it.hasNext() )
           arrayCondutores[i++] = it.next();
       Menu menu = new Menu(arrayCondutores);
    
       while( true){
            menu.executa();
            int opcao = menu.getOpcao();
            if( opcao == 0)
                break;
            estatisticas( arrayCondutores[opcao -1],db);
       }
    }
    // O condutor tem que ser TaxiDriver !!!!!
    private static void estatisticas(String condutor, DataBase db){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        
        TaxiDriver c = (TaxiDriver) db.getUser( condutor);
        Company empresa = db.getEmpresa(c.getEmpresa());
        Set<Travel> viagens = c.getViagensComOsVeiculos( empresa.getVeiculos(),db);
        
        double kmstotal = viagens.stream().mapToDouble(Travel::getDistCondutor)     
                                          .sum();
        int numViagens = viagens.size();
        double lucro = viagens.stream().mapToDouble(Travel::getPrecoReal)
                                       .sum();
                                              
                                
        StringBuilder sb = new StringBuilder();

        sb.append("\n---------------\nA trabalhar para a empresa: ");
        sb.append(c.getDispEmpresa() +"\n");
        sb.append("Kms feitos: ");
        sb.append(df.format(kmstotal) + "\n");
        sb.append("Numero de viagens feitas: ");
        sb.append(numViagens + "\n");
        sb.append("Lucro total feito na empresa: ");
        sb.append(df.format(lucro) + "\n---------------\n");
        
        System.out.println(sb.toString() ); 
    }
    
    private static void despedirCondutor(Company empresa, DataBase db){
        Scanner input = new Scanner(System.in);   
        
        System.out.println("Insira o mail do taxista");
        String mail = input.nextLine();
        if( ! empresa.existeCondutor(mail,db) ){
            System.out.println("Taxista não existente");
            return ;
        }
        
        TaxiDriver aux = (TaxiDriver) db.getUser( mail);
        db.taxiDriver2Driver(aux );
        empresa.removeTaxista( mail);
    }
    private static void adicionarCondutor( Company empresa, DataBase db){
       Scanner input = new Scanner(System.in);   
       
       System.out.println("Insira o mail do taxista");
       String mail = input.nextLine();
       if( ! db.existeUtilizador(mail) || db.getUser(mail) instanceof TaxiDriver || db.getUser(mail) instanceof Client){
            System.out.println("Mail não é valido");
            return ;
       }
       
       Driver d = (Driver) db.getUser(mail);
       
       db.driver2TaxiDriver( d, empresa.getNome());
       empresa.addCondutor(mail);

    }
    private static void estatisticas(Company empresa, DataBase db){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        
        StringBuilder sb = new StringBuilder();
        sb.append("  *** Estatisticas da empresa ***\n");
        sb.append("Lucro total: ");
        sb.append(df.format(empresa.lucroTotal(db)));
        sb.append("\nVeiculo com mais viagens: ");
        try{
            sb.append(empresa.veiculoComMaisViagens(db));
        }
        catch( CarroNaoExistenteException e ){ sb.append("Não ha veiculos"); }
        sb.append("\nCondutor com mais viagens: ");
        try{
            sb.append(empresa.taxistaComMaisViagens(db));
        }
        catch(UserNaoExistenteException e){ sb.append("Não ha taxistas"); }
        sb.append("\nTaxista com maior grau de cumprimento ");
        try{
            sb.append( empresa.taxistaComMelhorGrau(db));
        }
        catch(UserNaoExistenteException e){ sb.append(e.getMessage()); } 
        sb.append("\nTaxista com melhor classificação: ");
        try{
            sb.append(empresa.taxistaComMelhorClasificacao(db));
        }
        catch(UserNaoExistenteException e){ sb.append(e.getMessage()); } 
       
        System.out.println( sb.toString());
    }
                     
    
    //----------------------------------------------
    
  
}
