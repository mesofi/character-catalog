package com.mesofi.collection.charactercatalog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CharacterFigureController {

	private final CharacterRepository characterRepository;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/add")
	public String saveCharacter(@RequestBody CharacterFigure characterFigure) {
		characterRepository.save(characterFigure);
		return "Added with id: " + characterFigure.getId();
	}

	@GetMapping("/add")
	public List<CharacterFigure> findCharacter() {
		System.out.println("=========");
		//return characterRepository.findAllByOrderByReleaseDate();
		List<CharacterFigure>  l = characterRepository.findAll();
		System.out.println(l);
		return l;
	}

	@PatchMapping("/add/{id}")
	public String updateCharacter(@PathVariable String id, @RequestBody JsonPatch patch) {
/*
		Optional<CharacterFigure> found = characterRepository.findById(id);
		CharacterFigure customerPatched = null;
		try {
			customerPatched = applyPatchToCustomer(patch, found.get());
		} catch (JsonProcessingException | IllegalArgumentException | JsonPatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		characterRepository.save(customerPatched);
		return "ds";
		*/
		return null;
	}

	private CharacterFigure applyPatchToCustomer(JsonPatch patch, CharacterFigure characterFigure) throws JsonProcessingException, IllegalArgumentException, JsonPatchException {
		JsonNode patched = patch.apply(objectMapper.convertValue(characterFigure, JsonNode.class));
	    return objectMapper.treeToValue(patched, CharacterFigure.class);
		
	}
}
