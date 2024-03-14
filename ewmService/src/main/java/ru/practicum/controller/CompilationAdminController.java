package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    /**
     * <p>Добавление новой подборки.</p>
     * POST /admin/compilations
     *
     * @param newCompilationDto обновляющая подборка.
     * @return обновлённая подборка.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки. POST /admin/compilations {}", newCompilationDto);
        return compilationService.addCompilation(newCompilationDto);
    }

    /**
     * <p>Удаление подборки.</p>
     * DELETE /admin/compilations/{compId}
     *
     * @param compId ID удаляемой подборки.
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Long compId) {
        log.info("Удаление подборки. DELETE /admin/compilations compId {}", compId);
        compilationService.deleteCompilation(compId);
    }

    /**
     * <p>Обновление информации о подборке.</p>
     * PATCH /admin/compilations/{compId}
     *
     * @param compId                   ID подборки, которую необходимо обновить.
     * @param updateCompilationRequest данные для обновления подборки.
     * @return обновлённая подборка.
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("PATCH /admin/compilations compId {}, {}", compId, updateCompilationRequest);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
