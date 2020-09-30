
/**
 * Write a description of class Company here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.io.Serializable;
import java.util.OptionalDouble;

public class Company implements Serializable{
  
   // instance variables 
   
   private String nome;
   private String password;
   // matricula -> carro
   private Set<String> livres;
   private Set<String> ocupados;
   // mail -> TaxiDriver
   private Set<String> condutores; 
   
   //contrutores
   
   public Company(){
       this("","");
   }
   public Company(String nome, String password){
       this.nome = nome;
       this.password = password;
       livres = new HashSet<String>();
       ocupados = new HashSet<String>();
       condutores = new HashSet<String>();
   }
   public Company( Company c){
       this.nome = c.getNome();
       this.password = c.getPassword();
       this.livres = c.getLivres();
       this.ocupados = c.getOcupados();
       this.condutores = c.getCondutores();
    }
   
   //gets e sets
   
   public String getNome(){
       return nome;
   }
   private String getPassword(){
       return password;
   }
   public Set<String> getLivres(){
       return livres.stream().collect(Collectors.toSet());
   }
   public Set<String> getOcupados(){
       return ocupados.stream().collect(Collectors.toSet());
   }
   public Set<String> getVeiculos(){
       Set<String> res = new HashSet<String>();
       for( String x : ocupados)
          res.add(x);
       for( String x : livres)
          res.add(x);
        
       return res;
    }
   public Set<String> getCondutores(){
       return condutores.stream().collect(Collectors.toSet());
   }
    
   private void SetNome( String nome){
       this.nome = nome;
    }
   public void setPassWord(String nova, String antiga) throws PalavraPasseErradaException {
        if( validaLogin(antiga))
            this.password = nova;
        else
            throw new PalavraPasseErradaException();
        
   }
    
   //metodos obrigatorios
   
   public Company clone(){
       return new Company(this);
   }
   public int hashCode(){
       return nome.hashCode();
   }
   public boolean equals( Object o ){
       if ( this == o) return true;
       if ( o == null || o.getClass() != this.getClass() ) return false;
       
       Company c = (Company) o;
       
       return this.nome == c.nome; // && falta comparar os Sets!! 
    }
   public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("Nome da empresa: ");
       sb.append( nome + "\n" );
       sb.append("Veiculos: ");
       for( String x : getVeiculos())
        sb.append(x  +"  ");
       sb.append("\nCondutores: ");
       for( String x : condutores)
        sb.append(x + "  ") ;
       
       return sb.toString();
    }
   
    
   //adicionar e removar veiculos e condutores
    public void addCondutor(String taxidriver){
       condutores.add( taxidriver );
   }
   public void addTaxi(String matricula){
       livres.add( matricula );
   }
   public void removeVeiculo( String matricula){
      if( livres.contains( matricula))
        livres.remove(matricula);
      if( ocupados.contains(matricula))
        ocupados.remove(matricula);
    }
   public void removeTaxista( String mail){
       condutores.remove(mail);
    }
   public String disponibilizaCarro() throws CarroNaoExistenteException{
        if ( livres.size() == 0)
            throw new CarroNaoExistenteException();
        
        Iterator<String> it = livres.iterator();
        String carro = it.next();
        it.remove();
        ocupados.add(carro);
        return carro;
    }
   public void devolveCarro( String carro){
       ocupados.remove(carro);
       livres.add(carro);
   }
   
   //teste se existe condutores ou veiculo
   public boolean existeCondutor(String d, DataBase db){
       if ( !(db.getUser(d) instanceof TaxiDriver) ) return false;
       return condutores.contains(d) ;
   }
   public boolean existeVeiculo(String v){
       return getVeiculos().contains(v);
   }
   
   public boolean validaLogin(String pass){
       return this.password.equals(pass);
   }
   
   public int numDeTaxis(){
       return getVeiculos().size();
    }
   public int numDeCondutores(){
        return condutores.size();
    }
   
   
    
   // ------- estatisticas da empresa ---------------
    
   public double lucroTotal( DataBase db){
     
       return getVeiculos().stream().map((String matricula) ->  db.getVeiculo(matricula))
                                    .mapToDouble( (Veiculo x) -> x.lucroTotal(db))
                                    .sum();
    }
   public String veiculoComMaisViagens(DataBase db) throws CarroNaoExistenteException{
       if( getVeiculos().size() == 0)
            throw new CarroNaoExistenteException();
   
       Iterator<String> it = getVeiculos().iterator();
       
       Veiculo carro = db.getVeiculo(it.next());
       int max = carro.numDeViagens();
       
       
       while(it.hasNext()){
           Veiculo carroAux = db.getVeiculo( it.next());
           if( carroAux.numDeViagens() > max){
               max = carroAux.numDeViagens();
               carro = carroAux;
            }
       }
       
       return carro.getMatricula() + " - com " + max + " viagens";
    }
   public String taxistaComMaisViagens(DataBase db) throws UserNaoExistenteException{
       if( condutores.size() == 0)
        throw new UserNaoExistenteException();
   
       Iterator<String> it = condutores.iterator();
       
       Driver condutor = ( Driver) db.getUser(it.next());
       int max = condutor.getViagensComOsVeiculos(getVeiculos(),db).size();
    
       while(it.hasNext()){
           Driver condutorAux = (Driver) db.getUser( it.next());
           if( condutorAux.getViagensComOsVeiculos(getVeiculos(),db).size() > max){
               max = condutorAux.getViagensComOsVeiculos(getVeiculos(),db).size();
               condutor = condutorAux;
            }
       }
     
       return condutor.getEmail() + " - com " + max + " viagens";
    }
   public String taxistaComMelhorGrau(DataBase db) throws UserNaoExistenteException{
        if( condutores.size() == 0)
            throw new UserNaoExistenteException("Não tem condutores");
        Set<Driver> condutoresComViagens = condutores.stream()
                                        .map(x -> (Driver) db.getUser(x))
                                        .filter(x -> x.getViagensComOsVeiculos( this.getVeiculos(), db).size() !=0)
                                        .collect(Collectors.toSet());
        if( condutoresComViagens.size() == 0)
            throw new UserNaoExistenteException("Nenhum condutor tem viagens realizadas pela empresa");
            
        Iterator<Driver> it = condutoresComViagens.iterator();
        Driver condutor = it.next();
        Set<Travel> viagens = condutor.getViagensComOsVeiculos( getVeiculos(), db);
        long  viagensComSucesso = viagens.stream().filter( t -> t.getTempoEstimado() >= t.getTempoReal()).count();
        double max = (viagensComSucesso/viagens.size())*100;
                                                                                  
    
        while(it.hasNext()){
           Driver condutorAux = it.next();
           viagens = condutorAux.getViagensComOsVeiculos( getVeiculos(), db);
           viagensComSucesso = viagens.stream().filter( t -> t.getTempoEstimado() >= t.getTempoReal()).count();
           double grauAux = (viagensComSucesso/viagens.size()) *100;
           if( grauAux > max){
               max = grauAux;
               condutor = condutorAux;
           }
        }
       
        return condutor.getEmail() + " - com " + max + "%";
    }
   public String taxistaComMelhorClasificacao(DataBase db) throws UserNaoExistenteException{
        if( condutores.isEmpty())
            throw new UserNaoExistenteException("Não tem condutores");
            
        Iterator<String> it = condutores.iterator();
        double max = -1;
        Driver condutor = new Driver();
        /*
        Driver condutor = ( Driver) db.getUser(it.next());
        Set<Travel> viagens = condutor.getViagensComOsVeiculos( getVeiculos(), db);
            
        double max = viagens.stream().mapToInt(Travel::getNota)
                                     .filter( x -> x != -1)
                                     .average()
                                     .getAsDouble();
          */                           
        while(it.hasNext()){
           Driver condutorAux = (Driver) db.getUser( it.next());
           Set<Travel> viagens = condutorAux.getViagensComOsVeiculos( getVeiculos(), db);
           OptionalDouble opDouble = viagens.stream().mapToInt(Travel::getNota)
                                                     .filter( x -> x != -1)
                                                     .average();
           double classAux;
           if( opDouble.isPresent()) 
               classAux = opDouble.	getAsDouble();
           else
              continue;
           if( classAux > max){
               max = classAux;
               condutor = condutorAux;
            }
        }
        if( max == -1) 
            throw new UserNaoExistenteException("Nenhum condutor tem viagens pela empresa avaliadas");
        return condutor.getEmail() +" - com " + max +"%";
    }
    
}
