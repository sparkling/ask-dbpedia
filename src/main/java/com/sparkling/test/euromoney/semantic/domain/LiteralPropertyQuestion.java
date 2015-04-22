package com.sparkling.test.euromoney.semantic.domain;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class LiteralPropertyQuestion implements Question{
	private String domainName;
	private String predicateName;
	private String language;
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getPredicateName() {
		return predicateName;
	}
	public void setPredicateName(String predicateName) {
		this.predicateName = predicateName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
    @Override
    public String toString() {
    	return MoreObjects.toStringHelper(this)
                .add("domainName", getDomainName())
                .add("predicateName", getPredicateName())
                .add("languageName", getLanguage())
                .toString();
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(
        		getDomainName(),
        		getPredicateName(),
        		getLanguage()
        		);
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof LiteralPropertyQuestion){
        	LiteralPropertyQuestion qst = (LiteralPropertyQuestion) o;
            if (Objects.equals(qst.getDomainName(), getDomainName()) &&
                Objects.equals(qst.getPredicateName(), getPredicateName()) &&
                Objects.equals(qst.getLanguage(), getLanguage()))
            {
                return true;
            }
        }
        return false;
    }  
    
    public static Builder getBuilder(String domainName, String predicateName, String language) {
        return new Builder(domainName, predicateName).language(language);
    }
    
    public static Builder getBuilder(String domainName, String predicateName) {
        return new Builder(domainName, predicateName);
    }    
    
    public static class Builder {
        private LiteralPropertyQuestion built;
        
        public Builder(String domainName, String predicateName) 
        {
            built = new LiteralPropertyQuestion();
            built.setDomainName(domainName);
            built.setPredicateName(predicateName);
        }
        
        public Builder language(String language){
        	built.setLanguage(language);
        	return this;
        }

        public LiteralPropertyQuestion build(){
            return built;
        }
    }    
}
