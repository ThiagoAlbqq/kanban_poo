import models.KanbanModel;
import view.TelaInicialView;

public class Main {
    public static void main(String[] args) {
        KanbanModel model = KanbanModel.getInstancia();

        TelaInicialView view = new TelaInicialView();

        view.init(model);
    }
}