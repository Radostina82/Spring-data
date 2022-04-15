package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Override
    public boolean areImported() {
        return this.agentRepository.count()>0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "agents.json");
        return Files.readString(path);
    }

    @Override
    public String importAgents() throws IOException {
        String json = this.readAgentsFromFile();
        ImportAgentDTO[] importAgentDTOS = this.gson.fromJson(json, ImportAgentDTO[].class);
        return Arrays.stream(importAgentDTOS).map(this::importAgent)
                .collect(Collectors.joining("\n"));
    }

    private String importAgent(ImportAgentDTO agentDto) {
        Set<ConstraintViolation<ImportAgentDTO>> validateErrors = this.validator.validate(agentDto);
        if (!validateErrors.isEmpty()){
            return "Invalid agent";
        }

        Optional<Agent> byFirstName = this.agentRepository.findByFirstName(agentDto.getFirstName());
        Optional<Agent> byEmail = this.agentRepository.findByEmail(agentDto.getEmail());

        if (byFirstName.isPresent() || byEmail.isPresent()){
            return "Invalid agent";
        }

        Agent agent = this.modelMapper.map(agentDto, Agent.class);
        this.agentRepository.save(agent);

        return String.format("Successfully imported agent %s %s", agent.getFirstName(), agent.getLastName());
    }
}
