import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPostCardComponent } from './add-post-card.component';

describe('AddPostCardComponent', () => {
  let component: AddPostCardComponent;
  let fixture: ComponentFixture<AddPostCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddPostCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPostCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
