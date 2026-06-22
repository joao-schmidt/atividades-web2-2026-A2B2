package br.edu.ifpr.casalapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpr.casalapp.dto.TransacaoRequest;
import br.edu.ifpr.casalapp.dto.TransacaoResponse;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private int proximoId = 4;

    private final List<TransacaoResponse> transacoes = new ArrayList<>(List.of(
            new TransacaoResponse(1, "Salário", 5000.00, "receita"),
            new TransacaoResponse(2, "Aluguel", 1500.00, "despesa"),
            new TransacaoResponse(3, "Supermercado", 600.00, "despesa")));

    @GetMapping
    public List<TransacaoResponse> listarTransacoes() {
        return transacoes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponse> buscarTransacao(@PathVariable int id) {
        Optional<TransacaoResponse> transacaoEncontrada = transacoes.stream()
                .filter(t -> t.id() == id)
                .findFirst();

        return transacaoEncontrada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criarTransacao(@RequestBody TransacaoRequest request) {
        TransacaoResponse nova = new TransacaoResponse(proximoId, request.descricao(), request.valor(), request.tipo());
        proximoId++;
        transacoes.add(nova);
        return ResponseEntity.status(201).body(nova);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponse> atualizarTransacao(@PathVariable int id,
            @RequestBody TransacaoRequest request) {
        Optional<Integer> indexOpt = IntStream.range(0, transacoes.size())
                .filter(i -> transacoes.get(i).id() == id)
                .boxed()
                .findFirst();

        if (indexOpt.isPresent()) {
            int index = indexOpt.get();
            TransacaoResponse atualizada = new TransacaoResponse(id, request.descricao(), request.valor(),
                    request.tipo());
            transacoes.set(index, atualizada);
            return ResponseEntity.ok(atualizada);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransacaoResponse> atualizarParcialTransacao(@PathVariable int id,
            @RequestBody TransacaoRequest request) {
        Optional<Integer> indexOpt = IntStream.range(0, transacoes.size())
                .filter(i -> transacoes.get(i).id() == id)
                .boxed()
                .findFirst();

        if (indexOpt.isPresent()) {
            int index = indexOpt.get();
            TransacaoResponse existente = transacoes.get(index);

            String novaDescricao = request.descricao() != null ? request.descricao() : existente.descricao();
            Double novoValor = request.valor() != null ? request.valor() : existente.valor();
            String novoTipo = request.tipo() != null ? request.tipo() : existente.tipo();

            TransacaoResponse atualizada = new TransacaoResponse(id, novaDescricao, novoValor, novoTipo);
            transacoes.set(index, atualizada);
            return ResponseEntity.ok(atualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTransacao(@PathVariable int id) {
        boolean removido = transacoes.removeIf(t -> t.id() == id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}