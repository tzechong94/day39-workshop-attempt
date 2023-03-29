import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { Character } from 'src/app/model/models';
import { CharacterService } from 'src/app/service/character.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  characters?: Character[];

  constructor(private charSvc: CharacterService, private router: Router) {}


  previous() {

  }
  next() {

  }

  ngOnInit(): void {
      this.charSvc.getCharacters(this.charSvc.characterSearched).then(
        response => this.characters = response as Character[]
      )
      console.log(this.characters)
  }


}
