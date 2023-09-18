package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mesofi.collection.charactercatalog.model.LineUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;

@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {
    private CharacterFigureService characterFigureService;

    @BeforeEach
    public void init() {
        characterFigureService = new CharacterFigureService();
    }

    @Test
    public void should_AssertWithError_WheNull() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V3);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Final Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setPlainCloth(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Plain Clothes)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Golden Genealogy Pegasus Seiya");
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Golden Genealogy Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V4);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V3);
        characterFigure.setOce(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V2);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Power of Gold~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V4);
        characterFigure.setOce(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth) ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Initial Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya & Goddess Athena");
        characterFigure.setOce(true);
        characterFigure.setBroken(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya & Goddess Athena ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V1);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Limited Gold~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~New Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setAnniversary(10);
        characterFigure.setGroup(Group.V4);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth) ~10th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setOce(true);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setOce(true);
        characterFigure.setGroup(Group.V2);
        characterFigure.setAnniversary(40);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Original Color Edition) ~40th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.DDP);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V2);
        characterFigure.setGolden(true);
        characterFigure.setHk(true);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~ ~HK Version~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V5);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Heaven Chapter)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V2);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~", characterFigure.getDisplayedName());
        
        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V3);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Golden Limited Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        characterFigure.setAnniversary(20);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~20th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setManga(true);
        characterFigure.setAnniversary(20);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Comic Version) ~20th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Aries Shion");
        characterFigure.setSurplice(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Aries Shion (Surplice)", characterFigure.getDisplayedName());
    }
}
