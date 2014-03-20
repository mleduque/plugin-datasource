package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources.DatasourceUiStyle;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class RequestResultHeader extends DockLayoutPanel {

    private static final HeaderTemplate TEMPLATE            = GWT.create(HeaderTemplate.class);
    private static final int            INFO_HEADER_WIDTH   = 150;
    private static final int            EXPORT_BUTTON_WIDTH = 70;

    private static int                  TRUNCATE_LIMIT      = 150;

    private Widget                      infoHeaderTitle;
    private Widget                      queryReminder;
    private Button                      exportButton;
    private final DatasourceUiStyle     style;
    private final RequestResultDelegate delegate;

    public RequestResultHeader(final DatasourceUiStyle style,
                               final RequestResultDelegate delegate) {
        super(Unit.PX);
        this.style = style;
        this.delegate = delegate;
        setStyleName(style.infoHeader());
    }

    public RequestResultHeader setInfoHeaderTitle(final String label) {
        this.infoHeaderTitle = new HTML(TEMPLATE.infoHeaderTitle(style.infoHeaderTitle(), label));
        return this;
    }

    public RequestResultHeader setRequestReminder(final String query) {
        // limit size of displayed query - just a bit over display overflow
        final String queryPart = query.substring(0, Math.min(query.length(), TRUNCATE_LIMIT));

        this.queryReminder = new HTML(TEMPLATE.queryReminder(style.queryReminder(), queryPart));
        return this;
    }

    public RequestResultHeader withExportButton(final RequestResultDTO requestResult, final String text) {
        this.exportButton = new Button(text);
        this.exportButton.setStyleName(style.csvButton());
        this.exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                delegate.triggerCsvExport(requestResult);
            }
        });
        return this;
    }

    public RequestResultHeader prepare() {
        clear();
        addWest(this.infoHeaderTitle, INFO_HEADER_WIDTH);
        if (this.exportButton != null) {
            addEast(this.exportButton, EXPORT_BUTTON_WIDTH);
        }
        add(this.queryReminder);
        return this;
    }

    interface HeaderTemplate extends SafeHtmlTemplates {

        @Template("<div class='{0}'>{1}</div>")
        SafeHtml queryReminder(String className, String query);

        @Template("<span class='{0}'>{1}</span>")
        SafeHtml infoHeaderTitle(String className, String label);
    }

    /**
     * Interface for the control delegate for the RequestResultHeader actions.
     * 
     * @author "Mickaël Leduque"
     */
    public interface RequestResultDelegate {
        /**
         * Causes the given request result to be converted to CSV and sent to user.
         * 
         * @param requestResult the request result
         */
        void triggerCsvExport(RequestResultDTO requestResult);
    }
}
