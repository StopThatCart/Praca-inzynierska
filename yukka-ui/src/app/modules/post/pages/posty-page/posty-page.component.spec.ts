import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostyPageComponent } from './posty-page.component';

describe('PostyPageComponent', () => {
  let component: PostyPageComponent;
  let fixture: ComponentFixture<PostyPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostyPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostyPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
