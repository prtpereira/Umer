
/**
 * Write a description of class User here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class User implements Serializable,ImplementaViagens
{
    private String email;
    private String password;
    private String nome;
    private String morada;
    private LocalDate dataDeNascimento;
    
    private List<Integer> viagens;
    
   
    public User() {
        this("","","","",LocalDate.of(1970,1,1));
    }
    public User( String email,String nome, String password, String morada,LocalDate dataDeNascimento){
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.morada = morada;
        this.dataDeNascimento = dataDeNascimento;
        this.viagens = new ArrayList<Integer>();
       
    }
    public User(User a) {
        this.email = a.getEmail();
        this.nome = a.getNome();
        this.password = a.getPassword();
        this.morada = a.getMorada();
        this.dataDeNascimento = a.getDataDeNascimento();
        this.viagens = a.getViagens();
        
    }
    

    //gets e sets
    
     
    public String getNome(){
        return nome;
    }
    public String getEmail(){
        return email;
    }
    public String getMorada(){
        return morada;
    }
     public LocalDate getDataDeNascimento(){
        return dataDeNascimento;
    }
    public List<Integer> getViagens(){
        return viagens.stream().collect(Collectors.toList());
    } 
    private String getPassword(){
        return password;
    }
    
    public void setMorada(String morada){
        this.morada = morada;
    }
    public void setDataDeNascimento(LocalDate data){
        this.dataDeNascimento = data;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setPassWord(String nova, String antiga) throws PalavraPasseErradaException {
        if( validaLogin(antiga))
            this.password = nova;
        else
            throw new PalavraPasseErradaException();
        
    }
    private void setPassword(String pass){
        password = pass;
    }
    
    
    //metodos Obrigatorios
    
     public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("Email: ");
        sb.append(this.email);
        sb.append("\nNome: ");
        sb.append(this.nome);
        sb.append("\nMorada: ");
        sb.append(this.morada);
        sb.append("\nData de nascidemento: ");
        sb.append(this.dataDeNascimento);
      
        return sb.toString();
     }
    
    public boolean equals( Object o){
        if( o == this) return true;
        if( o == null || o.getClass() != this.getClass()) return false;
        
        User m = (User) o;
        
        return this.email.equals(m.getEmail()) && password.equals( m.getPassword()) 
                && nome.equals(m.getNome()) && morada.equals( m.getMorada()) && 
                dataDeNascimento.equals(m.getDataDeNascimento());
    }
 
    public int hashCode(){
        return email.hashCode() ;
    }
    
    public boolean validaLogin(String pass){
        return pass.equals( password );
    }
    public void addViagem(int t){
        viagens.add(t);      
    }
    public List<String> listaViagens(DataBase db){
        List<String> lista = new ArrayList<String>();
        for( int i = 0; i < viagens.size(); i ++)
            lista.add(db.getViagem(i).resumo());
        return lista;
    }
    public List<String> listaViagens(DataBase db, LocalDate inicio, LocalDate fim){
        return getViagens().stream().map(i -> db.getViagem(i))
                                    .filter( t ->t.getData().isBefore(fim) && t.getData().isAfter(inicio))
                                    .map(Travel::resumo)
                                    .collect(Collectors.toList());
        
    }
    public int numDeViagens(){
        return viagens.size();
    }
    public int getViagem( int x){
        return viagens.get(x);
    }

}
