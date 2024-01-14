package com.novidades.gestaodeprojetos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novidades.gestaodeprojetos.model.Avaliacao;
import com.novidades.gestaodeprojetos.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

    @Autowired
    public AvaliacaoRepository avaliacaoRepository;

    public List<Avaliacao> obterTodos (){
       return avaliacaoRepository.findAll();
    }

    public Optional<Avaliacao> obterPorId(Long id){
       return avaliacaoRepository.findById(id);
    }

    public Avaliacao adicionar (Avaliacao avaliacao){
        return avaliacaoRepository.save(avaliacao);
    }
    
    public Avaliacao atualizar (Long id,Avaliacao avaliacao){
        avaliacao.setId(id);
        return avaliacaoRepository.save(avaliacao);
    }

    public void deletar (Long id){
         avaliacaoRepository.deleteById(id);
    }
}
