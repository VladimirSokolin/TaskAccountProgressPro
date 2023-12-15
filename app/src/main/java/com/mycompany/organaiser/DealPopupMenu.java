package com.mycompany.organaiser;

import android.app.FragmentManager;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;


public class DealPopupMenu extends PopupMenu {
    DialogConfirmationer confirmationer;
    DeleteDealListener deleteDealListener;
    EditDealListener editDealListener;
    public DealPopupMenu(final Context context, View view, final FragmentManager fm) {
        super(context, view);
        confirmationer = new DialogConfirmationImpl(context);
        getMenu().add("Delete");
        getMenu().add("Edit");

        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem){

                if(menuItem.getTitle().toString().equals("Edit")){
                    editDealListener.editDeal();
                } else if (menuItem.getTitle().toString().equals("Delete")) {
                    confirmationer.showDialogConfirmation(
                        "Confirmation",
                            "You want to delete this?",
                            "Ok",
                            "Cancel",
                            ()->{
                                if(deleteDealListener != null)
                                    deleteDealListener.deleteDeal();
                            }
                    );
                }
                return false;
            }
        });
    }

    public void setDeleteDealListener(DeleteDealListener listener){
        deleteDealListener = listener;
    }
    public void setEditDealListener(EditDealListener listener){
        editDealListener = listener;
    }

}
