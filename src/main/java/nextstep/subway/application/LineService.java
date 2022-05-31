package nextstep.subway.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.NotFoundException;
import nextstep.subway.domain.Line;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.SectionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line persistLine = lineRepository.save(lineRequest.toLine());
        persistLine.addSection(generateSection(lineRequest.getUpStationId(), lineRequest.getDownStationId(),
                lineRequest.getDistance()));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findOneLine(long id) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundException::new);
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundException::new);
        line.changeLineInfo(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void saveSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(NotFoundException::new);
        line.addSection(generateSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance()));
    }

    private Section generateSection(long upStationId, long downStationId, long distance) {
        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(NotFoundException::new);
        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(NotFoundException::new);
        return sectionRepository.save(new Section(upStation, downStation, distance));
    }
}
