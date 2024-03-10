package ru.practicum.dto.event;

import javax.persistence.Embeddable;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class LocationDto {

    private Float lat;

    private Float lon;
}
