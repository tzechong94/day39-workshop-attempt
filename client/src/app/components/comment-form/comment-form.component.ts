import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CharacterService } from 'src/app/service/character.service';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.css']
})
export class CommentFormComponent implements OnInit {

  form!: FormGroup
  newComment!: string
  characterName!: string
  characterId!: string 
constructor(private fb: FormBuilder, 
  private charSvc: CharacterService, private router: Router,
  private activatedRoute: ActivatedRoute){}

  ngOnInit(): void {
    this.form = this.createForm();
    this.characterId = this.activatedRoute.snapshot.params['characterId']
    this.characterName = this.charSvc.currentCharacter
    console.log('character id: ', this.characterId)
  }

  postCommentById() {
    this.newComment = this.form.value.comment
    console.log('newcomment: ', this.newComment)
    this.charSvc.postCommentById(this.characterId, this.newComment)
    this.router.navigate(["../"])
  }

  createForm() {
    return this.fb.group({
      comment: this.fb.control<string>('', Validators.required)
    })
  }
  

}
