package abalone.protocol;

/**
 * Defines the methods that the Abalone Server should support while connected.
 * 
 * @version {@link ProtocolMessages#VERSION}
 * @author Daan Pluister, Bozhidar Petrov
 */
public interface ServerProtocol {

    /**
     * generates an error message given errorMessage and the errorType.
     * error types are be:
     * - type 1: Unexpected command: The command letter is not available.
     * - type 2: Unexpected argument: One or more arguments is invalid.
     * - type 3: Illegal argument Performing this action is expected but
     * not allowed. The server will indicate what is wrong. Examples: not your turn, lobby is full, etc.
     * 
     * @requires errorMessage != null
     * @requires errorType \in \{1,2,3\}
     * @return String to be returned to the client
     */
    public String doError(int errorType, String errorMessage);


}
