import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndeksusComponent } from './indeksus.component';

describe('IndeksusComponent', () => {
  let component: IndeksusComponent;
  let fixture: ComponentFixture<IndeksusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndeksusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndeksusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
