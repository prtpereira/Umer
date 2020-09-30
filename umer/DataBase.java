
/**
 * Write a description of class DataBase here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.Collection;

public class DataBase implements Serializable{
    
   private Map<String,User> users;  // mail -> User
   private Map<String,Veiculo> veiculos;  // matricula -> Veiculo
   private Map<String,Company> empresas; //nome -> Company
   private List<Travel> viagens; // (ArrayList) integer -> Travel 
   private Map<Integer,Travel> emEspera; // viagens que estao por acabar ( em lista de espera)
   
   //funçao para testes!!!! 
   public String imprimeTUDO(){
       StringBuilder sb = new StringBuilder();
       for( User u : users.values()){
           sb.append( u.toString());
           sb.append("\n----------------\n");
        }
       /*
        for( Veiculo v : veiculos.values()){
          sb.append( v.toString());
          sb.append("\n----------------\n");
       }
       */
       
       return sb.toString();
   }

   public DataBase(){
       users = new HashMap<String,User>();
       veiculos = new HashMap<String,Veiculo>();
       empresas = new HashMap<String,Company>();
       viagens = new ArrayList<Travel>();
       emEspera = new HashMap<Integer,Travel>();
    }
  
   public User getUser( String mail) {
       return users.get(mail);
   }
   public Veiculo getVeiculo( String matricula){
       return veiculos.get(matricula);
   }
   public Company getEmpresa( String empresa){
       return empresas.get(empresa);
   }
   public Travel getViagem( int index){
       return viagens.get( index);
    }
   public Travel getERemoveViageEmEspera( int hashcode){
       return emEspera.remove( hashcode);
    }
    
   public boolean existeUtilizador(String mail){
       return users.containsKey(mail);
   }
   public boolean existeVeiculo(String matricula){
       return veiculos.containsKey(matricula);
   }
   public boolean existeEmpresa( String empresa){
       return empresas.containsKey( empresa);
   }
   public boolean existeViagem( int index){
       return viagens.size() > index;
   }
   public boolean alguemConduz( String matricula){
       return users.values().stream().filter(x -> x instanceof Driver)
                                     .map( u -> (Driver) u)
                                     .anyMatch( x -> x.getVeiculo().equals(matricula));
   }
   
   public void add(User u){
       users.put(u.getEmail(), u);
   }  
   public void add(Veiculo v){
       veiculos.put(v.getMatricula(), v);
   }
   public void add(Company c){
       empresas.put(c.getNome(), c);
   }
   public int add(Travel t){
       viagens.add(t);
       return viagens.size() -1;
    }
   public void addListaDeEspera(Travel t){
       emEspera.put( t.hashCode(),t);
    }
    
   
   public Driver DriverMaisPerto(Ponto2D y) throws CondutorMaisProximoException{
       double aux = 100000;
       Driver result = new Driver();
       boolean flag = true; // para testar se foi encontrado algum carro disponivel
       
       Iterator<User> it = users.values().iterator();
       
       while( it.hasNext() ) {
           User u = it.next();
           if( u instanceof Driver){
                Driver d = (Driver) u;
                Ponto2D x = this.getVeiculo(d.getVeiculo()).getPosicao();
                if( y.distance(x) < aux && d.estaDisponivel() ){
                    aux = y.distance(x);
                    result = d;
                    flag = false;
                }
           }
       }
    
       if( flag )
            throw new CondutorMaisProximoException();
        
       return result;
    }
   
   public Driver getDriver( Veiculo v)throws CarroNaoExistenteException{
       boolean flag = true;
       Driver result = new Driver();
       
       Iterator<User> it = users.values().iterator();
       
         while( it.hasNext() ) {
           User u = it.next();
           if( u instanceof Driver){
                Driver d = (Driver) u;
                Veiculo x = getVeiculo(d.getVeiculo());
                if( v.equals(x) && d.estaDisponivel()){
                    result = d;
                    flag = false;
                }
           }
       }
       
       if( flag )
            throw new CarroNaoExistenteException();
        
       return result;
   }
   public void driver2TaxiDriver( Driver d, String empresa){
       TaxiDriver t = new TaxiDriver(d,empresa,"",false);
       users.put( t.getEmail(), t);
   }
   public void taxiDriver2Driver( TaxiDriver td){
       Driver novo = new Driver(td);
       this.add( novo);
   }
   
   public void gravaEmObjStream(String fich) throws IOException{
       ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(fich));
       oout.writeObject(this);
       oout.flush(); 
       oout.close();
   }
   
   
   //-----------------------------------------------------
   
   
   public List<Client> top10(){
       List<Client> lista = new ArrayList<Client>();
       int cap = 0;
       double min = 0;
       int indexMin = 0;
       Iterator it = users.values().iterator();
       
       while( it.hasNext() && cap < 10){
           User u = (User) it.next();
           if( u instanceof Client){
               Client c = (Client) u;
               lista.add(cap,c);
               if( c.getGasto(this) < min){
                 min = c.getGasto(this);
                 indexMin = cap;
               }
               cap ++;
            }
       }
       while( it.hasNext() ){
           User u = (User) it.next();
           if( u instanceof Client){
               Client c = (Client) u;
               if( c.getGasto(this) > min){
                   lista.remove(indexMin);
                   lista.add(c);
                   min = lista.stream().mapToDouble( x -> x.getGasto(this)).min().getAsDouble();
                   indexMin = indexDoMenor( lista);
                }
            }
        }
            
       return lista; //nao ordenada
    }
    private int indexDoMenor( List<Client> lista){
        int res = 0;
        double comp = lista.get(0).getGasto(this);
        int i =0;
        for( Client c : lista){
            if ( c.getGasto(this) < comp){
                comp = c.getGasto(this);
                res = i;
            }
            i++;
        }
        return res;
    }
    public List<Driver> PioresCondutores(){
       List<Driver> lista = new ArrayList<Driver>();
       int cap = 0;
       double max = 200; // varia entre 0 - 100, por isso este valor serve
       int indexMax = 0;
       Iterator it = users.values().iterator();
       
       while( it.hasNext() && cap < 5){
           User u = (User) it.next();
           if( u instanceof Driver){
               Driver d = (Driver) u;
               lista.add(cap,d);
               if( d.getGrauCumprimento() < max){
                 max = d.getGrauCumprimento();
                 indexMax = cap;
               }
               cap ++;
            }
       }
       while( it.hasNext() ){
           User u = (User) it.next();
           if( u instanceof Driver){
               Driver d = (Driver) u;
               if( d.getGrauCumprimento() < max){
                   lista.remove(indexMax);
                   lista.add(d);
                   max = lista.stream().mapToDouble( x -> x.getGrauCumprimento()).max().getAsDouble();
                   indexMax = indexDoMaior( lista);
                }
            }
        }
            
       return lista; //nao ordenada
    }
    private int indexDoMaior( List<Driver> lista){
        int res = 0;
        double comp = lista.get(0).getGrauCumprimento();
        int i =0;
        for( Driver c : lista){
            if ( c.getGrauCumprimento() > comp){
                comp = c.getGrauCumprimento();
                res = i;
            }
            i++;
        }
        return res;
    }
       public void PopularDataBase(){
       Veiculo v1 = new Ligeiro("12-AF-33", 4.32, 56, new Ponto2D(-2,-2));
       Veiculo v2 = new Ligeiro("ZZ-12-85", 5.97, 78, new Ponto2D(-7,0));
       Veiculo v3 = new Ligeiro("48-BJ-59", 3.4, 67, new Ponto2D(-2,0));
       Veiculo v4 = new Ligeiro("03-JG-49", 4.9, 69, new Ponto2D(26,2));
       Veiculo v5 = new Ligeiro("52-AD-13", 4.5, 67, new Ponto2D(3,12));
       Veiculo v6 = new Mota("AO-03-32", 3.3, 80, new Ponto2D(0,0));
       Veiculo v7 = new Carrinha("74-12-GA", 6.3, 56, new Ponto2D(2,2));
       Veiculo v8 = new Carrinha("66-65-FJ", 5.6, 54, new Ponto2D(12,0));
       Veiculo v9 = new Carrinha("11-11-QQ", 4.1, 53, new Ponto2D(1,-3));
       
       Driver d1 = new Driver("jose@umer.pt","Jose Gonçalves","pass","Bastuço",LocalDate.of(1975,12,3), true, "11-11-QQ");
       Driver d2 = new Driver("watson@umer.pt","Andre Pereira","pass","Povoa de lanhoso",LocalDate.of(1990,3,26), true,"66-65-FJ");
       Driver d3 = new Driver("manetes@umer.pt","Paulo Sergio","pass","Famalicao",LocalDate.of(1985,3,17), false,"74-12-GA");
       Driver d4 = new Driver("julia@umer.pt","Julia Pires","pass","Viseu",LocalDate.of(1963,7,13), true, "AO-03-32");
       Driver d5 = new Driver("maria_1253@umer.pt","Maria Alexandra","pass","Lisboa",LocalDate.of(1988,3,10), true,"52-AD-13");
       Driver d6 = new Driver("jorge@umer.pt","Jorge Martins","pass","Maia",LocalDate.of(1967,11,11), false,"03-JG-49");
       Driver d7 = new Driver("sandro@umer.pt","Sandro Cruz","pass","Barcelos",LocalDate.of(1997,1,15), true,"48-BJ-59");
       Driver d8 = new Driver("pedro@umer.pt","Pedro Miguel","pass","Braga",LocalDate.of(1990,8,28), true,"ZZ-12-85");
       Driver d9 = new Driver("egor@umer.pt","Egor das Antas","pass","Leiria",LocalDate.of(1989,4,17), false,"12-AF-33");
       
       
       Client c1 = new Client("antonio@umer.pt","Antonio Pinto","pass","Braga",LocalDate.of(1996,7,27), new Ponto2D(1,1));
       Client c2 = new Client("quim@umer.pt","Joaquim Oliveira","pass","Porto",LocalDate.of(1967,10,6), new Ponto2D(4,5));
       Client c3 = new Client("collina@umer.pt","Tiago Pinto","pass","Barcelos",LocalDate.of(1956,4,17), new Ponto2D(4,5));
       Client c4 = new Client("tiago@umer.pt","Tiago Taveira","pass","Prado",LocalDate.of(1985,3,11), new Ponto2D(4,5));
       Client c5 = new Client("jesusCristo12@umer.pt","Marco Paulo","pass","Braga",LocalDate.of(1976,9,29), new Ponto2D(4,5));
       Client c6 = new Client("joana@umer.pt","Joana Rocha","pass","Leiria",LocalDate.of(1990,3,15), new Ponto2D(4,5));
       Client c7 = new Client("calinhos@umer.pt","Carlos Mantorras","pass","Amares",LocalDate.of(1975,10,1), new Ponto2D(4,5));
       Client c8 = new Client("obonito@umer.pt","Filipe Amorim","pass","Povoa de Lanhoso",LocalDate.of(1970,1,1), new Ponto2D(4,5));
       Client c9 = new Client("semnome@umer.pt","Antonio da Costa","pass","Porto",LocalDate.of(1959,12,2), new Ponto2D(4,5));
       Client c10 = new Client("sonia@umer.pt","Sonia Arantes","pass","Braga",LocalDate.of(1966,6,27), new Ponto2D(4,5));
       Client c11 = new Client("nelson@umer.pt","Nelson Simoes","pass","Porto",LocalDate.of(1978,5,14), new Ponto2D(4,5));
       Client c12 = new Client("ze@umer.pt","Jose Amaro","pass","Famalicão",LocalDate.of(1975,8,25), new Ponto2D(4,5));   
       
    
       
       add(v1); add(v2); add(v3); add(v4); add(v5); add(v6); add(v7); add(v8); add(v9);
       add(d1); add(d2) ; add(d3); add(d4); add(d5) ; add(d6); add(d7); add(d8) ; add(d9); 
       add(c1) ; add(c2); add(c3) ; add(c4); add(c5) ; add(c6); add(c7) ; add(c8); add(c9) ; add(c10); add(c11) ; add(c12);
       //------------------------------------------
       
       Company cp2 = new Company("Orlando Taxis","pass");
       
       Veiculo v10 = new Ligeiro("PP-12-65", 3.5, 78, new Ponto2D(3,8));
       Veiculo v11 = new Ligeiro("PG-79-64", 4.5, 53, new Ponto2D(-4,6));
       Veiculo v12 = new Ligeiro("43-43-GQ", 6.2, 70, new Ponto2D(-1,5));
 
       TaxiDriver td1 = new TaxiDriver("artur@umer.pt","Artur Miguel","pass","Porto",LocalDate.of(1989,4,17), false,"PP-12-65","Orlando Taxis","",false);
       TaxiDriver td2 = new TaxiDriver("carlos@umer.pt","Carlos Pinto","pass","Braga",LocalDate.of(1975,8,25), false,"PG-79-64","Orlando Taxis","",false);
       TaxiDriver td3 = new TaxiDriver("edgar@umer.pt","Edgar Santos","pass","Braga",LocalDate.of(1975,8,25), false,"43-43-GQ","Orlando Taxis","",false);
       
       Veiculo v20 = new Ligeiro("TY-22-21", 4.5, 70, new Ponto2D(3,1));
       Veiculo v21 = new Ligeiro("22-98-HF", 4.7, 68.6, new Ponto2D(-2,1));
       Veiculo v22 = new Ligeiro("98-00-FQ", 4.0, 56, new Ponto2D(3,-2));
       
       add(v10);add(v11);add(v12);add(v20);add(v21);add(v22);add(td1);add(td2);add(td3);
       
       cp2.addCondutor(td1.getEmail()); cp2.addCondutor(td2.getEmail()); cp2.addCondutor(td3.getEmail());
       cp2.addTaxi( v20.getMatricula()); cp2.addTaxi( v21.getMatricula()); cp2.addTaxi( v22.getMatricula());
       
       add(cp2);
       //-----------------------------------------
       
    }
   
}