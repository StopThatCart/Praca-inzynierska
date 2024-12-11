import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameIconComponent } from './rename-icon.component';

describe('RenameIconComponent', () => {
  let component: RenameIconComponent;
  let fixture: ComponentFixture<RenameIconComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenameIconComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenameIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
