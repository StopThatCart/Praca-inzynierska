import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CechaTagComponent } from './cecha-tag.component';

describe('CechaTagComponent', () => {
  let component: CechaTagComponent;
  let fixture: ComponentFixture<CechaTagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CechaTagComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CechaTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
