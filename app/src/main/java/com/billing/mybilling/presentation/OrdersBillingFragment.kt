package com.billing.mybilling.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.databinding.FragmentOrdersBillingBinding
import com.billing.mybilling.databinding.FragmentPendingOrdersDetailsBinding
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.billing.mybilling.utils.setPrice
import com.billing.mybilling.utils.showFormDialog
import com.billing.mybilling.utils.showWarningDialog
import com.billing.mybilling.utils.updateMobileDialog
import com.google.gson.Gson
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class OrdersBillingFragment: BaseFragment<FragmentOrdersBillingBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    var myFinalAmount = 0
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().fragment = this

        homeViewModel.totalAmount.observe(viewLifecycleOwner){
            getViewDataBinding().totalPrice.text = (it).toString().setPrice()

            val customerDiscount = homeViewModel.pendingOrders?.customer_discount ?: 0
            val deliveryCharges = homeViewModel.pendingOrders?.delivery_charges ?: 0
            val finalAmount = it - customerDiscount + deliveryCharges
            myFinalAmount = finalAmount
            homeViewModel.finalAmount = "$finalAmount"
            getViewDataBinding().name.text = homeViewModel.pendingOrders?.customer_name
            getViewDataBinding().totalDiscount.text = "- ${customerDiscount.toString().setPrice()}"
            getViewDataBinding().deliveryCharge.text = homeViewModel.pendingOrders?.delivery_charges.toString().setPrice()
            getViewDataBinding().finalAmount.text = finalAmount.toString().setPrice()
            if (homeViewModel.isPaymentOnline.value!!){
                homeViewModel.pendingOrders?.receiveOnline = myFinalAmount.toString()
                homeViewModel.pendingOrders?.receiveCash = "0"
            }else{
                homeViewModel.pendingOrders?.receiveCash = myFinalAmount.toString()
                homeViewModel.pendingOrders?.receiveOnline = "0"
            }
        }

    }

    fun addDiscount(){
          findNavController().navigate(OrdersBillingFragmentDirections.actionOrdersBillingFragmentToAddDiscountFormFragment(editType.DISCOUNT.type, Gson().toJson(homeViewModel.pendingOrders)))
    }

   fun completeCheckout(isShare: Boolean){
       if(isShare){
           val contact = homeViewModel.pendingOrders?.customer_contact?:""
            if(contact.isEmpty()){
                showToast("Please add customer Whats app number for this")
            }else{
                checkOut(isShare)
            }
       }else{
           checkOut(isShare)
       }

   }

    fun checkOut(isShare:Boolean){
        showLoading(true)
        homeViewModel.pendingOrders?.order_status = OrderStatus.DELIVERED.status
        homeViewModel.updatePendingOrder(SelectedAction.UPDATE_STATUS.type,homeViewModel.pendingOrders).observe(viewLifecycleOwner){
            it.getValueOrNull()?.let {
                showLoading(false)
                if(it.status==1){
                    showToast("Order completed successfully")
//                            findNavController().popBackStack()
                    if (isShare){
                        shareOnWhatsApp()
                    }else
                    findNavController().navigate(OrdersBillingFragmentDirections.actionOrdersBillingFragmentToHomeFragment())

                }
            }
        }

    }

    private fun shareOnWhatsApp() {
        val user = sessionManager.getUser()
        val pdfFileName = "${user?.business_name+homeViewModel.pendingOrders?.order_id}.pdf"
        val directory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filePath = File(directory, pdfFileName)
        try {
            val model = homeViewModel.pendingOrders
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            document.setPageSize(PageSize.A4)

            document.addCreationDate()
            document.addAuthor("EasyBill")
            document.addCreator(user?.business_name)
            //font setting
            val colorAccent = BaseColor(0,153,204,255)
            val fontSize = 18.0f
            val valueFontSize = 18.0f
            //Custom Font
            val fontName = BaseFont.createFont("res/font/poppins_regular.ttf","UTF-8", BaseFont.EMBEDDED)
            val topTitleFontName = BaseFont.createFont("res/font/grillmedium.otf","UTF-8", BaseFont.EMBEDDED)

            val packageName = "com.billing.mybilling"
            val uri = FileProvider.getUriForFile(requireContext(), "${packageName}.provider", filePath)

            val drawable = resources.getDrawable(R.drawable.billlogo4)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image: Image = Image.getInstance(stream.toByteArray())
//            image.alignment = Image.MIDDLE
            document.add(image)

            //Create Title of document
            val titleFont = Font(fontName,18.0f, Font.NORMAL, BaseColor.BLACK)
            val topTitleFont = Font(topTitleFontName,40.0f,Font.NORMAL,BaseColor.RED)
            addNewItem(document,"${user?.business_name}", Element.ALIGN_CENTER,topTitleFont)

            //Add more
            val orderNumberFont = Font(fontName,fontSize,Font.NORMAL,colorAccent)
            addNewItem(document,"Order Id:",Element.ALIGN_LEFT,orderNumberFont)

            val orderNumberValueFont = Font(fontName,valueFontSize,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"${model?.order_id}",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont)
            addNewItem(document,"${model?.timestamp}",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            addNewItem(document,"Customer Name:",Element.ALIGN_LEFT,orderNumberFont)
            addNewItem(document,"${model?.customer_name}",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            //Add Product order detail
            addLineSpace(document)
            addNewItem(document,"Order Detail",Element.ALIGN_CENTER,titleFont)
            addLineSeparator(document)
            var counter = 1
            model?.products?.forEach {products->

                    //Item1
                    val price = products.price?.toInt()?:0
                    val finalPrice = products.selected_quan * price
                    addNewItemWithLeftAndRight(document,"$counter. "+products.product_name,"Quan :  ${products.selected_quan} X Rs. $price",titleFont,orderNumberValueFont)
                    addNewItemWithLeftAndRight(document,"    "+products.variant_name.toString(),"Rs. ${finalPrice}",titleFont,orderNumberValueFont)
                    addLineSeparator(document)
                    counter++
            }



            //Total
            addLineSpace(document)
            addLineSpace(document)

            val orderFinalValueFont = Font(fontName,18.0f,Font.BOLD,BaseColor.BLACK)
            val discount = homeViewModel.pendingOrders?.customer_discount?:0
//            val finalPrice = homeViewModel.finalAmount.toInt() - discount
            addNewItemWithLeftAndRight(document,"    Discount","Rs. -${discount}",orderFinalValueFont,orderFinalValueFont)
            addLineSeparator(document)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"    Grand Total","Rs. ${homeViewModel.finalAmount}",orderFinalValueFont,orderFinalValueFont)

            addLineSpace(document)
            document.add(Paragraph(" "))
            document.add(Paragraph(" "))
            document.add(Paragraph(" "))
            val fontThankYou = BaseFont.createFont("res/font/poppins_regular.ttf","UTF-8", BaseFont.EMBEDDED)
            val orderThankYouFont = Font(fontThankYou,14.0f,Font.NORMAL,BaseColor.BLACK)
            addNewItem(document,"** Thank you for visiting ${user?.business_name} **",Element.ALIGN_RIGHT,orderThankYouFont)
            document.close()

//            val number = "+916378999146"
            val number = homeViewModel.pendingOrders?.customer_contact
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_SEND)
            i.putExtra(Intent.EXTRA_STREAM, uri)
            i.putExtra(Intent.EXTRA_TEXT, "this is working")
            i.type = "application/pdf"
//            i.data = Uri.parse(url)
            i.putExtra("jid", "91$number" + "@s.whatsapp.net")
            startActivity(i)
            findNavController().navigate(OrdersBillingFragmentDirections.actionOrdersBillingFragmentToHomeFragment())
        }catch (e:Exception){
            Log.d("myLogError", "shareOnWhatsApp: ${e.message}")
            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
        }
        }


    private fun addNewItemWithLeftAndRight(document: Document, textLeft: String, textRight: String, textLeftFont: Font,textRightFont:Font) {
        val chunkTextLeft = Chunk(textLeft,textLeftFont)
        val chunkTextRight = Chunk(textRight,textRightFont)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)

    }
    fun addOnline(){
        val dialog = showFormDialog(
            requireActivity(),
            layoutInflater,
            "Receive Online",myFinalAmount) {
            val cash = myFinalAmount-it.toInt()
            getViewDataBinding().receiveOnline.setPrice(it)
            getViewDataBinding().receiveCash.setPrice(cash.toString())
            homeViewModel.pendingOrders?.receiveCash = cash.toString()
            homeViewModel.pendingOrders?.receiveOnline = it
        }
        dialog.show()
    }

    fun addMobileNumber(){
        val dialog = updateMobileDialog(
            requireActivity(),
            layoutInflater,
            "Mobile Number",homeViewModel.pendingOrders?.customer_contact) {
            homeViewModel.pendingOrders?.customer_contact = it
            getViewDataBinding().mobile.text = it
            homeViewModel.updatePendingOrder(SelectedAction.UPDATE.type,homeViewModel.pendingOrders).observe(viewLifecycleOwner){
                it?.getValueOrNull()?.let {

                }
            }
        }
        dialog.show()
    }

    fun addCashPayment(){
        val dialog = showFormDialog(
            requireActivity(),
            layoutInflater,
            "Receive Cash",myFinalAmount) {
            val online = myFinalAmount-it.toInt()
            getViewDataBinding().receiveOnline.setPrice(online.toString())
            getViewDataBinding().receiveCash.setPrice(it)
            homeViewModel.pendingOrders?.receiveOnline = online.toString()
            homeViewModel.pendingOrders?.receiveCash = it

        }
        dialog.show()
    }



    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    private fun addNewItem(document: Document, text: String, alignCenter: Int, font: Font) {
        val chunk = Chunk(text,font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignCenter
        document.add(paragraph)
    }
    override fun getLayoutId() = R.layout.fragment_orders_billing
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}