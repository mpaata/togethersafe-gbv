# DESKTOP.md --- JavaFX Developer Specialist Document

## 1. Who This Is For
You are the **JavaFX Developer** on TogetherSafe.
You build the desktop app that survivors, caseworkers, and admins use.

You work alongside:
- **Backend Lead** --- provides the REST API you call
- **Database + DevOps** --- owns the schema your models mirror
- **Product + QA** --- tests your screens

You own the desktop app. You do not edit backend code or Python AI code.

## 2. What You Build in v1
- JavaFX desktop application for Windows, macOS, and Linux
- Login screen with JWT session handling
- Survivor screens (Home, SOS Center, Report Case, My Cases, Evidence Vault, Find Help)
- Admin screens (Dashboard, Case Management, Urgent Alerts, Referral Tracking)
- Disguised Mode (JNA integration to hide taskbar/process)
- FXML layouts styled with JavaFX CSS
- API client to talk to the Spring Boot backend
- Background tasks (no blocking the UI thread)
- Toast notifications and modals

**Not in v1:** Offline mode, push notifications, 1:1 messaging, forums. Those are v2+.

## 3. Package Structure
```text
desktop/src/main/java/com/togethersafe/desktop/
|
|-- DesktopApplication.java
|
|-- config/
|   |-- AppConfig.java       <- backend URL, settings
|   |-- Session.java         <- current user, JWT token
|   |-- SceneManager.java    <- navigation between screens
|   +-- ApiConfig.java
|
|-- api/
|   |-- ApiClient.java       <- HTTP wrapper (java.net.http.HttpClient)
|   |-- AuthApi.java
|   |-- CaseApi.java
|   |-- EvidenceApi.java
|   |-- ReferralApi.java
|   +-- AiApi.java           <- calls Python FastAPI service
|
|-- model/
|   |-- User.java
|   |-- Case.java
|   |-- Evidence.java
|   |-- Referral.java
|   +-- Service.java
|
|-- controller/
|   |-- LoginController.java
|   |-- MainLayoutController.java
|   |-- survivor/
|   |   |-- SurvivorHomeController.java
|   |   |-- SosCenterController.java
|   |   |-- ReportCaseController.java
|   |   |-- MyCasesController.java
|   |   +-- EvidenceVaultController.java
|   +-- admin/
|       |-- AdminDashboardController.java
|       |-- CaseManagementController.java
|       +-- UrgentAlertsController.java
|
|-- service/
|   |-- AuthService.java
|   |-- CaseService.java
|   |-- EvidenceService.java
|   +-- AiService.java
|
|-- util/
|   |-- Toast.java           <- toast notifications
|   |-- Modal.java           <- dialog helpers
|   |-- FormValidator.java
|   |-- TaskRunner.java      <- background tasks
|   +-- DisguisedModeUtil.java <- JNA integration
|
+-- pdf/
    +-- ReportViewer.java

    desktop/src/main/resources/
|-- fxml/
|   |-- login.fxml
|   |-- main-layout.fxml
|   |-- survivor/
|   +-- admin/
|-- css/
|   |-- styles.css
|   +-- components.css
|-- icons/
|   +-- app-icon.ico
+-- config.properties    


4. Code Conventions
FXML files
kebab-case names: case-vault.fxml

fx:id uses camelCase matching the controller field: fx:id="caseTable" → @FXML TableView<Case> caseTable;

One root container per FXML

Use VBox, HBox, BorderPane, GridPane for layout

Use TableView for tabular data

No inline styles --- use CSS classes

Example skeleton:

xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.control.*?>

<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.togethersafe.desktop.controller.survivor.MyCasesController"
            styleClass="my-cases">
    <top>
        <HBox styleClass="page-header" spacing="16">
            <Label fx:id="titleLabel" styleClass="h1" text="My Cases"/>
            <Region HBox.hgrow="ALWAYS"/>
            <Button fx:id="newCaseButton" text="+ New Case" styleClass="btn-primary"/>
        </HBox>
    </top>
    <center>
        <TableView fx:id="caseTable">
            <columns>
                <TableColumn fx:id="caseIdCol" text="Case ID"/>
                <TableColumn fx:id="typeCol" text="Type"/>
                <TableColumn fx:id="statusCol" text="Status"/>
                <TableColumn fx:id="dateCol" text="Date Reported"/>
            </columns>
        </TableView>
    </center>
</BorderPane>
Controllers
Annotate fields with @FXML --- always

Implement Initializable and use initialize()

Use @FXML private void onSomeAction(ActionEvent event)

Never call backend on the JavaFX thread --- use TaskRunner

Show errors via Toast or Modal

Log at DEBUG for user actions

Example:

java
@Component
@RequiredArgsConstructor
public class MyCasesController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private TableView<Case> caseTable;
    @FXML private TableColumn<Case, String> caseIdCol;
    @FXML private TableColumn<Case, String> typeCol;
    @FXML private TableColumn<Case, String> statusCol;
    @FXML private TableColumn<Case, String> dateCol;
    @FXML private Button newCaseButton;

    private final CaseService caseService;
    private ObservableList<Case> cases = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupButtons();
        loadCases();
    }

    private void setupTable() {
        caseIdCol.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateReported"));
        caseTable.setItems(cases);
    }

    private void setupButtons() {
        newCaseButton.setOnAction(e -> SceneManager.switchTo("report-case"));
    }

    private void loadCases() {
        TaskRunner.run(
            () -> caseService.getMyCases(),
            this::populateTable,
            err -> Toast.error(err.getMessage())
        );
    }

    private void populateTable(List<Case> data) {
        cases.setAll(data);
    }
}
API Client
java
@Component
@RequiredArgsConstructor
public class ApiClient {
    private final Session session;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl = "http://localhost:8080/api/v1";

    public <T> T get(String path, Class<T> type) {
        return exchange("GET", path, null, type);
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return exchange("POST", path, body, type);
    }

    private <T> T exchange(String method, String path, Object body, Class<T> type) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");

            if (session.getToken() != null) {
                builder.header("Authorization", "Bearer " + session.getToken());
            }

            String json = body == null ? "" : mapper.writeValueAsString(body);
            builder.method(method, HttpRequest.BodyPublishers.ofString(json));

            HttpResponse<String> response = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw ApiException.from(response);
            }

            ApiResponse<T> wrapper = mapper.readValue(response.body(),
                mapper.getTypeFactory().constructParametricType(ApiResponse.class, type));
            return wrapper.getData();
        } catch (IOException | InterruptedException e) {
            throw new ApiException("Network error: " + e.getMessage(), e);
        }
    }
}
TaskRunner (Background Tasks)
java
public class TaskRunner {
    public static <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Exception> onError) {
        Task<T> task = new Task<>() {
            @Override protected T call() throws Exception { return work.call(); }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept((Exception) task.getException()));
        Thread thread = new Thread(task, "api-task");
        thread.setDaemon(true);
        thread.start();
    }
}
5. Key UI Components for TogetherSafe
SOS Button (Survivor Home)
Large, red, prominent button.

Instantly triggers an emergency action (e.g., opens dialer, sends alert, or shows emergency contacts).

Must be accessible with one click/tap.

Disguised Mode (JNA)
A toggle in Settings.

When enabled, changes the window title to something neutral (e.g., "System Update").

Uses JNA to hide the application from the Windows Taskbar (ITaskbarList) and changes the process name.

Important: This must be tested carefully. Antivirus software may flag it.

Evidence Vault Table
Columns: File Name, Type, Uploaded Date, Shared With.

Actions: View, Download, Share.

Sharing opens a modal where the survivor explicitly selects which caseworker/organization can see it.

6. Testing
Unit tests: Test service classes (Mock the API client).

UI tests (TestFX): Test navigation, form validation, and critical workflows (e.g., login, report case, SOS button).

Manual testing: Test on Windows, macOS, and Linux. Test Disguised Mode on a real machine.

7. Packaging
Use jpackage to create native installers:

bash
jpackage \
  --type msi \
  --name "TogetherSafe" \
  --input target/ \
  --main-jar desktop-1.0.0.jar \
  --main-class com.togethersafe.desktop.DesktopApplication \
  --icon src/main/resources/icons/app-icon.ico \
  --app-version 1.0.0 \
  --vendor "TogetherSafe"
8. Definition of Done (Desktop)
A desktop task is done when:

□ FXML matches the design system (colors, fonts, spacing).
□ Controller follows DESKTOP.md patterns.
□ API calls use TaskRunner --- no blocking on FX thread.
□ Toast for success, modal for errors.
□ Keyboard navigation works (Tab, Enter, Escape).
□ Empty state and loading state implemented.
□ Error state implemented with plain-language message.
□ Tested on real Windows/macOS/Linux PC.
□ PR reviewed and approved.
9. What NOT to Do
Don't do network calls on the JavaFX thread.

Don't put business logic in controllers --- call services.

Don't use System.out.println --- use SLF4J.

Don't hardcode colors or fonts --- use CSS classes.

Don't inline styles in FXML --- use CSS.

Don't call the API client directly from controllers --- go through services.

Don't skip loading states --- always show feedback within 200ms.

Don't swallow exceptions --- show a toast and log.