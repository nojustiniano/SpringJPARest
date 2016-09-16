package hello;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class HelloController {
	
	@PostMapping(path="/validation", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> greeting(@Valid @RequestBody Person person, BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode jsonError = mapper.createObjectNode();
			ArrayNode jsonArray = jsonError.putArray("errors");
			
			for (FieldError fe : bindingResult.getFieldErrors()) {
				ObjectNode node = mapper.createObjectNode();;
				node.put("field", fe.getField());
				node.put("error", fe.getDefaultMessage());
				jsonArray.add(node);
			}
			
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonError);
		}
    	   
    	return ResponseEntity.ok(person);
    }
}
