import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LulekComponent } from './lulek.component';

describe('LulekComponent', () => {
  let component: LulekComponent;
  let fixture: ComponentFixture<LulekComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LulekComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LulekComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
