import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Character } from 'src/app/model/models';
import { CharacterService } from 'src/app/service/character.service';

@Component({
  selector: 'app-character',
  templateUrl: './character.component.html',
  styleUrls: ['./character.component.css']
})
export class CharacterComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute, private charSvc: CharacterService, private router: Router) {}

  character!: Character

  ngOnInit(): void {
      const characterId = this.activatedRoute.snapshot.params['characterId']
      console.log(characterId)

      this.charSvc.getCharacterById(characterId).then(
        response => this.character = response as Character
        // response => console.log(response)
      )
      console.log(this.character)
  }

  commentButton() {
    this.charSvc.currentCharacter = this.character.name

  }

  

}
